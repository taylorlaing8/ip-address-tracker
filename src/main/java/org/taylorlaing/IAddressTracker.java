package org.taylorlaing;

import java.util.*;

public interface IAddressTracker {
    void requestHandled(String ipAddress);
    List<String> top100();
    void clear();
}
