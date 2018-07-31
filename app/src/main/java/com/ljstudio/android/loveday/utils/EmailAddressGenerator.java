package com.ljstudio.android.loveday.utils;

public class EmailAddressGenerator extends GenericGenerator {
    private static GenericGenerator instance = new EmailAddressGenerator();

    private EmailAddressGenerator() {
    }

    public static GenericGenerator getInstance() {
        return instance;
    }

    @Override
    public String generate() {
        StringBuilder result = new StringBuilder();
        result.append(RandomStringUtils.randomAlphanumeric(16));
        result.append("@");
        result.append(RandomStringUtils.randomAlphanumeric(5));
        result.append(".");
        result.append(RandomStringUtils.randomAlphanumeric(3));

        return result.toString().toLowerCase();
    }
}
