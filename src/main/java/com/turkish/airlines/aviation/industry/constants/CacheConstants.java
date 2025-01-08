package com.turkish.airlines.aviation.industry.constants;

public final class CacheConstants {
    
    private CacheConstants() {
    }

    public static final String SINGLE_TRANSPORTATION = "singleTransportation";
    public static final String ALL_TRANSPORTATIONS = "allTransportations";
    public static final String ROUTES = "routes";

    public static final int SINGLE_TRANSPORTATION_TTL = 5;
    public static final int ALL_TRANSPORTATIONS_TTL = 5;
    public static final int ROUTES_TTL = 5;
} 