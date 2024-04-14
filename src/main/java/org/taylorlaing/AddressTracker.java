package org.taylorlaing;

import java.util.*;

public class AddressTracker implements IAddressTracker {
    private final Map<String, Integer> ipAddressMap;
    private Queue<String> frequencyQueue;

    public AddressTracker() {
        this.ipAddressMap = new HashMap<>();
        this.frequencyQueue = new PriorityQueue<>((ip1, ip2) -> Integer.compare(this.ipAddressMap.get(ip1), this.ipAddressMap.get(ip2)));
    }

    public void requestHandled(String ipAddress) {
        int frequency = this.ipAddressMap.getOrDefault(ipAddress, 0) + 1;
        int currentLowestFrequency = this.ipAddressMap.getOrDefault(this.frequencyQueue.peek(), 0);

        // Update or add frequency for incoming ipAddress
        this.ipAddressMap.put(ipAddress, frequency);

        // Remove ipAddress from the queue (adding it back in if there are less than 100 or its frequency beats lowest in the queue)
        this.frequencyQueue.remove(ipAddress);

        if (this.frequencyQueue.size() < 100) {
            this.frequencyQueue.add(ipAddress);
        }
        else {
            if (frequency >= currentLowestFrequency) {
                this.frequencyQueue.poll();

                this.frequencyQueue.add(ipAddress);
            }
        }
    }

    public List<String> top100() {
        Queue<String> tempFrequencyQueue = new PriorityQueue<>((ip1, ip2) -> Integer.compare(this.ipAddressMap.get(ip1), this.ipAddressMap.get(ip2)));
        List<String> frequencyQueueList = new ArrayList<>();

        while (!this.frequencyQueue.isEmpty()) {
            String ipAddress = this.frequencyQueue.poll();

            tempFrequencyQueue.add(ipAddress);
            frequencyQueueList.add(ipAddress);
        }

        this.frequencyQueue = tempFrequencyQueue;

        return frequencyQueueList.reversed();
    }

    public void clear() {
        this.ipAddressMap.clear();
        this.frequencyQueue.clear();
    }

    // FOR TESTING PURPOSES ONLY
    public boolean top100ListInOrderOfFrequency(List<String> ipAddresses) {
        boolean inOrder = true;
        int lastFrequency = 0;

        for(String address : ipAddresses) {
            int frequency = this.ipAddressMap.get(address);

            if (lastFrequency != 0 && frequency > lastFrequency) {
                inOrder = false;
                break;
            }

            lastFrequency = frequency;
        }

        return inOrder;
    }
}