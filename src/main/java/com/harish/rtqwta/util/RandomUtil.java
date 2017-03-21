package com.harish.rtqwta.util;

import java.util.Date;
import java.util.Random;

public class RandomUtil {
	private static Random random = new Random();
	public static int getRandomInt(int min, int max){
		return random.nextInt(max-min) + min;
	}
	public static Date getRandomDate(Date minDate, Date maxDate) {
		if (minDate.compareTo(maxDate) == 0) {
            return minDate;
        }
        final long beginTime = minDate.getTime();
        final long endTime = maxDate.getTime();// + TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS);
        final long lNewDate = RandomUtil.getRandomPrimitiveLong(beginTime, endTime);
        if (beginTime < endTime) {
            if (lNewDate < beginTime) {
                return minDate;
            } else if (lNewDate > endTime) {
                return maxDate;
            }
        } else {
            if (lNewDate < endTime) {
                return minDate;
            } else if (lNewDate > beginTime) {
                return maxDate;
            }
        }
        return new Date(lNewDate);
    }
	public static long getRandomPrimitiveLong(long minRange, long maxRange) {
        if (minRange == maxRange) {
            return minRange;
        } else {
            long value = Long.valueOf(minRange + (long) (Math.random() * (maxRange - minRange + 1)));
            if (minRange < maxRange) {
                if (value < minRange) {
                    return minRange;
                } else if (value > maxRange) {
                    return maxRange;
                }
            } else {
                if (value < maxRange) {
                    return minRange;
                } else if (value > minRange) {
                    return maxRange;
                }
            }
            if ((minRange < 0 || maxRange < 0) && !getPositiveOrNegative() && value * -1 >= minRange && value * -1 <= maxRange) {
                return value * -1;
            } else {
                return value;
            }
        }
    }
	private static boolean getPositiveOrNegative() {
        int lRandomNumber = (int) (Math.random() * 3);
        return lRandomNumber % 2 == 0;
    }
}
