package org.taylorlaing;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        // Initialize tracker
        AddressTracker tracker = new AddressTracker();

        testAddressTracker(tracker, 1, 1000); // 1K
        testAddressTracker(tracker, 2, 10000); // 10K
        testAddressTracker(tracker, 3, 100000); // 100K
        testAddressTracker(tracker, 4, 1000000); // 1M
        testAddressTracker(tracker, 5, 10000000); // 10M
        testAddressTracker(tracker, 6, 20000000); // 20M
    }

    private static void testAddressTracker(AddressTracker tracker, int testNumber, int numberOfRecords) {
        System.out.println("Program start | Test: " + testNumber);
        System.out.println("Number of records to process: " + numberOfRecords);

        // Start fresh tracking session
        tracker.clear();

        // Generate list of 20M records
        System.out.println("Start generating list...");
        List<String> addressList = new ArrayList<>();
        for (int i = 1; i <= numberOfRecords; i++) {
            addressList.add("192.168." + testNumber + "." + i);
        }
        System.out.println("List generated");

        // Select random set of 100 records to repeat
        Random r = new Random();
        int low = 10;
        int high = numberOfRecords - 100;
        int randomStart = r.nextInt(high - low) + low;
        List<String> correctRepeatList = new ArrayList<>();
        for (int i = randomStart; i < randomStart + 100; i++) {
            correctRepeatList.add(addressList.get(i));

            // Repeat ipAddress a random number of times
            int min = 1;
            int max = 50;
            int randomCount = r.nextInt(max - min) + min;
            for (int j = 0; j < randomCount; j++) {
                addressList.add(addressList.get(i));
            }
        }

        // Shuffle collection
        Collections.shuffle(addressList);

        // Start timer to track total time to process IP Addresses with requestHandled function
        long startHandleTime = System.nanoTime();

        // Process IP Addresses
        for (String address : addressList) {
            tracker.requestHandled(address);
        }

        // End timer for requestHandled
        long endHandleTime = System.nanoTime();
        double handleDuration = (endHandleTime - startHandleTime) / (1000000.00 * 1000);
        System.out.println("Time to process requests: " + handleDuration + " seconds");

        // Start timer to track total time to process fetching the top 100 repeat records
        long startTopTime = System.nanoTime();

        List<String> top100List = tracker.top100();

        // End timer for fetching top 100 records
        long endTopTime = System.nanoTime();
        double topDuration = (endTopTime - startTopTime) / 1000000.00;
        System.out.println("Time to retrieve top 100 repeated IP Addresses: " + topDuration + " milliseconds");

        // Check that the returned list contains the correct items
        boolean containsAll = correctRepeatList.size() == top100List.size();
        for (String address : top100List) {
            boolean tempContains = correctRepeatList.contains(address);

            if (containsAll) {
                containsAll = tempContains;
            }
        }
        System.out.println("Top 100 list contains expected items: " + containsAll);

        // Check that the returned list is in order
        boolean listInOrder = tracker.top100ListInOrderOfFrequency(top100List);
        System.out.println("Top 100 list returned in order: " + listInOrder + "\n");
    }
}