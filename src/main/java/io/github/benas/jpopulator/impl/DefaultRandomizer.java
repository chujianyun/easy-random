/*
 * The MIT License
 *
 *   Copyright (c) 2014, Mahmoud Ben Hassine (md.benhassine@gmail.com)
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *   THE SOFTWARE.
 */

package io.github.benas.jpopulator.impl;

import io.github.benas.jpopulator.randomizers.DateRangeRandomizer;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * This class is used to generate random value for java built-in types.
 *
 * @author Mahmoud Ben Hassine (md.benhassine@gmail.com)
 * @author Nikola Milivojevic (0dziga0@gmail.com)
 */
final class DefaultRandomizer {

    private DefaultRandomizer() { }

    /**
     * The Random object to use.
     */
    private static final Random RANDOM;

    /**
     * The random date randomizer used to populate date types.
     */
    private static final DateRangeRandomizer dateRangeRandomizer;

    static {
        RANDOM = new Random();

        //initialise date randomizer to generate dates in [now - 10 years, now + 10 years]
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 10);
        Date inTenYears = calendar.getTime();
        calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -10);
        Date tenYearsAgo = calendar.getTime();
        dateRangeRandomizer = new DateRangeRandomizer(tenYearsAgo, inTenYears);

    }

    /**
     * Generate a random value for the given type.
     *
     * @param type the type for which a random value will be generated
     * @return a random value for the given type or null if the type is not supported
     */
    public static Object getRandomValue(final Class type) {

        /*
         * String and Character types
         */
        if (type.equals(String.class)) {
            return RandomStringUtils.randomAlphabetic(10);
        }
        if (type.equals(Character.TYPE) || type.equals(Character.class)) {
            return RandomStringUtils.randomAlphabetic(1).charAt(0);
        }

        /*
         * Boolean type
         */
        if (type.equals(Boolean.TYPE) || type.equals(Boolean.class)) {
            return RANDOM.nextBoolean();
        }

        /*
         * Numeric types
         */
        if (type.equals(Byte.TYPE) || type.equals(Byte.class)) {
            return (byte) (RANDOM.nextInt());
        }
        if (type.equals(Short.TYPE) || type.equals(Short.class)) {
            return (short) (RANDOM.nextInt());
        }
        if (type.equals(Integer.TYPE) || type.equals(Integer.class)) {
            return RANDOM.nextInt();
        }
        if (type.equals(Long.TYPE) || type.equals(Long.class)) {
            return RANDOM.nextLong();
        }
        if (type.equals(Double.TYPE) || type.equals(Double.class)) {
            return RANDOM.nextDouble();
        }
        if (type.equals(Float.TYPE) || type.equals(Float.class)) {
            return RANDOM.nextFloat();
        }
        if (type.equals(BigInteger.class)) {
            return new BigInteger(Math.abs(RANDOM.nextInt(100)), RANDOM);
        }
        if (type.equals(BigDecimal.class)) {
            return new BigDecimal(RANDOM.nextDouble());
        }
        if (type.equals(AtomicLong.class)) {
            return new AtomicLong(RANDOM.nextLong());
        }
        if (type.equals(AtomicInteger.class)) {
            return new AtomicInteger(RANDOM.nextInt());
        }

        /*
         * Date and time types
         */
        if (type.equals(java.util.Date.class)) {
            return new java.util.Date(dateRangeRandomizer.getRandomValue().getTime());
        }
        if (type.equals(java.sql.Date.class)) {
            return new java.sql.Date(dateRangeRandomizer.getRandomValue().getTime());
        }
        if (type.equals(java.sql.Time.class)) {
            return new java.sql.Time(RANDOM.nextLong());
        }
        if (type.equals(java.sql.Timestamp.class)) {
            return new java.sql.Timestamp(dateRangeRandomizer.getRandomValue().getTime());
        }
        if (type.equals(Calendar.class)) {
            return Calendar.getInstance();
        }
        if (type.equals(org.joda.time.DateTime.class)) {
        	return new org.joda.time.DateTime(dateRangeRandomizer.getRandomValue().getTime());
        }
        if (type.equals(org.joda.time.LocalDate.class)) {
        	return new org.joda.time.LocalDate(dateRangeRandomizer.getRandomValue().getTime());
        }
        if (type.equals(org.joda.time.LocalTime.class)) {
        	return new org.joda.time.LocalTime(dateRangeRandomizer.getRandomValue().getTime());
        }
        if (type.equals(org.joda.time.LocalDateTime.class)) {
        	return new org.joda.time.LocalDateTime(dateRangeRandomizer.getRandomValue().getTime());
        }
        if (type.equals(org.joda.time.Duration.class)) {
        	return new org.joda.time.Duration(Math.abs(RANDOM.nextLong()));
        }
        if (type.equals(org.joda.time.Period.class)) {
        	return new org.joda.time.Period(Math.abs(RANDOM.nextInt()));
        }
        if (type.equals(org.joda.time.Interval.class)) {
        	long startDate = Math.abs(RANDOM.nextInt());
        	long endDate = startDate + Math.abs(RANDOM.nextInt());
    		return new org.joda.time.Interval(startDate, endDate);
        }

        /*
         * Enum type
         */
        if (type.isEnum() && type.getEnumConstants().length > 0) {
            Object[] enumConstants = type.getEnumConstants();
            return enumConstants[RANDOM.nextInt(enumConstants.length)];
        }

        /*
         * Return null for any unsupported type
         */
        return null;

    }

}
