package net.puzzleco.gattapp.attributes;

import java.util.UUID;

public class GattAttributes {
    private static final String UUID_STRING_TEST_SERVICE = "0000f00d-1212-efde-1523-785fef13d123";

    private static final String UUID_STRING_CHARACTERISTICS_1 = "0000bef0-1212-efde-1523-785fef13d123";
    private static final String UUID_STRING_CHARACTERISTICS_2 = "0000beef-1212-efde-1523-785fef13d123";

    private static final String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";

    public final static UUID SERVICE = UUID.fromString(GattAttributes.UUID_STRING_TEST_SERVICE);
    public final static UUID CONFIG = UUID.fromString(GattAttributes.CLIENT_CHARACTERISTIC_CONFIG);

    public final static UUID CHARACTERISTICS_1 = UUID.fromString(GattAttributes.UUID_STRING_CHARACTERISTICS_1);
    public final static UUID CHARACTERISTICS_2 = UUID.fromString(GattAttributes.UUID_STRING_CHARACTERISTICS_2);
}
