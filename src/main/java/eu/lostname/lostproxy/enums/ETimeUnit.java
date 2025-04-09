/*
 * Copyright notice
 * Copyright (c) Nils Körting-Eberhardt 2021
 * Created: 16.01.2021 @ 22:29:30
 *
 * All contents of this source code are protected by copyright. The copyright is owned by Nils Körting-Eberhardt, unless explicitly stated otherwise. All rights reserved.
 *
 * ETimeUnit.java is part of the LostProxy which is licensed under the Attribution-NonCommercial-ShareAlike 4.0 International (CC BY-NC-SA 4.0) license.
 */

/*
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

/*
 *
 *
 *
 *
 *
 * Written by Doug Lea with assistance from members of JCP JSR-166
 * Expert Group and released to the public domain, as explained at
 * http://creativecommons.org/publicdomain/zero/1.0/
 */

package eu.lostname.lostproxy.enums;

/**
 * A {@code TimeUnit} represents time durations at a given unit of
 * granularity and provides utility methods to convert across units,
 * and to perform timing and delay operations in these units.  A
 * {@code TimeUnit} does not maintain time information, but only
 * helps organize and use time representations that may be maintained
 * separately across various contexts.  A nanosecond is defined as one
 * thousandth of a microsecond, a microsecond as one thousandth of a
 * millisecond, a millisecond as one thousandth of a second, a minute
 * as sixty seconds, an hour as sixty minutes, and a day as twenty four
 * hours.
 *
 * <p>A {@code TimeUnit} is mainly used to inform time-based methods
 * how a given timing parameter should be interpreted. For example,
 * the following code will timeout in 50 milliseconds if the {@link
 * java.util.concurrent.locks.Lock lock} is not available:
 *
 * <pre> {@code
 * Lock lock = ...;
 * if (lock.tryLock(50L, TimeUnit.MILLISECONDS)) ...}</pre>
 * <p>
 * while this code will timeout in 50 seconds:
 * <pre> {@code
 * Lock lock = ...;
 * if (lock.tryLock(50L, TimeUnit.SECONDS)) ...}</pre>
 * <p>
 * Note however, that there is no guarantee that a particular timeout
 * implementation will be able to notice the passage of time at the
 * same granularity as the given {@code TimeUnit}.
 *
 * @author Doug Lea
 * @since 1.5
 */
public enum ETimeUnit {
    /**
     * Time unit representing one thousandth of a microsecond.
     */
    NANOSECONDS(ETimeUnit.NANO_SCALE),
    /**
     * Time unit representing one thousandth of a millisecond.
     */
    MICROSECONDS(ETimeUnit.MICRO_SCALE),
    /**
     * Time unit representing one thousandth of a second.
     */
    MILLISECONDS(ETimeUnit.MILLI_SCALE),
    /**
     * Time unit representing one second.
     */
    SECONDS(ETimeUnit.SECOND_SCALE),
    /**
     * Time unit representing sixty seconds.
     *
     * @since 1.6
     */
    MINUTES(ETimeUnit.MINUTE_SCALE),
    /**
     * Time unit representing sixty minutes.
     *
     * @since 1.6
     */
    HOURS(ETimeUnit.HOUR_SCALE),
    /**
     * Time unit representing twenty four hours.
     *
     * @since 1.6
     */
    DAYS(ETimeUnit.DAY_SCALE),
    /**
     * Time unit representing seven days.
     *
     * @since 1.6
     */
    WEEKS(ETimeUnit.WEEK_SCALE);


    // Scales as constants
    private static final long NANO_SCALE = 1L;
    private static final long MICRO_SCALE = 1000L * NANO_SCALE;
    private static final long MILLI_SCALE = 1000L * MICRO_SCALE;
    private static final long SECOND_SCALE = 1000L * MILLI_SCALE;
    private static final long MINUTE_SCALE = 60L * SECOND_SCALE;
    private static final long HOUR_SCALE = 60L * MINUTE_SCALE;
    private static final long DAY_SCALE = 24L * HOUR_SCALE;
    private static final long WEEK_SCALE = 7L * DAY_SCALE;

    /*
     * Instances cache conversion ratios and saturation cutoffs for
     * the units up through SECONDS. Other cases compute them, in
     * method cvt.
     */

    private final long scale;
    private final long maxNanos;
    private final long maxMicros;
    private final long maxMillis;
    private final long maxSecs;
    private final long microRatio;
    private final int milliRatio;   // fits in 32 bits
    private final int secRatio;     // fits in 32 bits

    ETimeUnit(long s) {
        this.scale = s;
        this.maxNanos = Long.MAX_VALUE / s;
        long ur = (s >= MICRO_SCALE) ? (s / MICRO_SCALE) : (MICRO_SCALE / s);
        this.microRatio = ur;
        this.maxMicros = Long.MAX_VALUE / ur;
        long mr = (s >= MILLI_SCALE) ? (s / MILLI_SCALE) : (MILLI_SCALE / s);
        this.milliRatio = (int) mr;
        this.maxMillis = Long.MAX_VALUE / mr;
        long sr = (s >= SECOND_SCALE) ? (s / SECOND_SCALE) : (SECOND_SCALE / s);
        this.secRatio = (int) sr;
        this.maxSecs = Long.MAX_VALUE / sr;
    }

    public static String getDisplayName(long l, ETimeUnit eTimeUnit) {
        switch (eTimeUnit) {
            case NANOSECONDS:
                if (l == 1) {
                    return "Nanosekunde";
                } else {
                    return "Nanosekunden";
                }
            case MICROSECONDS:
                if (l == 1) {
                    return "Mikrosekunde";
                } else {
                    return "Mikrosekunden";
                }
            case MILLISECONDS:
                if (l == 1) {
                    return "Millisekunde";
                } else {
                    return "Millisekunden";
                }
            case SECONDS:
                if (l == 1) {
                    return "Sekunde";
                } else {
                    return "Sekunden";
                }
            case MINUTES:
                if (l == 1) {
                    return "Minute";
                } else {
                    return "Minuten";
                }
            case HOURS:
                if (l == 1) {
                    return "Stunde";
                } else {
                    return "Stunden";
                }
            case DAYS:
                if (l == 1) {
                    return "Tag";
                } else {
                    return "Tage";
                }
            case WEEKS:
                if (l == 1) {
                    return "Woche";
                } else {
                    return "Wochen";
                }
            default:
                return "";
        }
    }

    /**
     * Equivalent to
     *
     * @param duration the duration
     * @return the converted duration,
     * or {@code Long.MIN_VALUE} if conversion would negatively overflow,
     * or {@code Long.MAX_VALUE} if it would positively overflow.
     */
    public long toMillis(long duration) {
        long s, m;
        if ((s = scale) <= MILLI_SCALE)
            return (s == MILLI_SCALE) ? duration : duration / milliRatio;
        else if (duration > (m = maxMillis))
            return Long.MAX_VALUE;
        else if (duration < -m)
            return Long.MIN_VALUE;
        else
            return duration * milliRatio;
    }
}
