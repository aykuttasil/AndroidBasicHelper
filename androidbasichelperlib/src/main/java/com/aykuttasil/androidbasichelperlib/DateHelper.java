package com.aykuttasil.androidbasichelperlib;

import android.os.SystemClock;
import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by aykutasil on 10.02.2017.
 */

public class DateHelper {

    private static final String JUST_NOW = "Şimdi";
    private static final String MINUTES = " dakika";
    private static final String HOURS = " saat";
    private static final String H = "s";
    private static final String AGO = " önce";
    private static final String YESTERDAY = "Dün";

    public static boolean isSameDay(Long timestamp) {
        Calendar calendarForCurrent = Calendar.getInstance();
        Calendar calendarForScheduled = Calendar.getInstance();
        Date currentDate = new Date();
        Date date = new Date(timestamp);
        calendarForCurrent.setTime(currentDate);
        calendarForScheduled.setTime(date);
        return calendarForCurrent.get(Calendar.YEAR) == calendarForScheduled.get(Calendar.YEAR) &&
                calendarForCurrent.get(Calendar.DAY_OF_YEAR) == calendarForScheduled.get(Calendar.DAY_OF_YEAR);
    }

    public static String getFormattedDate(Long timestamp) {
        // boolean sameDay = isSameDay(timestamp);
        Date date = new Date(timestamp);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aa");
        SimpleDateFormat fullDateFormat = new SimpleDateFormat("dd MMM");
        return simpleDateFormat.format(date);
    }

    public static String getDate(Long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat fullDateFormat = new SimpleDateFormat("dd MMM yyyy");
        return fullDateFormat.format(date);
    }

    public static long getTimeDiffFromUtc() {
        SntpClient sntpClient = new SntpClient();
        long diff = 0;
        if (sntpClient.requestTime("0.africa.pool.ntp.org", 30000)) {
            long utcTime = sntpClient.getNtpTime() + SystemClock.elapsedRealtime() - sntpClient.getNtpTimeReference();
            diff = utcTime - System.currentTimeMillis();
        }
        return diff;
    }

    public static String getFormattedDateAndTime(Long timestamp) {
        boolean sameDay = isSameDay(timestamp);
        Date date = new Date(timestamp);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aa");
        SimpleDateFormat fullDateFormat = new SimpleDateFormat("dd MMM");
        Date newDate = new Date();

        try {
            if (sameDay) {
                long currentTime = newDate.getTime() - date.getTime();
                long diffMinutes = TimeUnit.MILLISECONDS.toMinutes(currentTime);
                long diffHours = TimeUnit.MILLISECONDS.toHours(currentTime);
                if (diffMinutes <= 1 && diffHours == 0) {
                    return JUST_NOW;
                }
                if (diffMinutes <= 59 && diffHours == 0) {
                    return String.valueOf(diffMinutes) + MINUTES;
                }
                if (diffHours <= 2) {
                    return String.valueOf(diffHours) + H;
                }
                return simpleDateFormat.format(date);
            }
            return fullDateFormat.format(date);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getDateAndTimeForLastSeen(Long timestamp) {
        boolean sameDay = isSameDay(timestamp);
        Date date = new Date(timestamp);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, MMM dd,yyyy");

        try {
            if (sameDay) {
                Date newDate = new Date();
                long currentTime = newDate.getTime() - date.getTime();
                long diffMinutes = TimeUnit.MILLISECONDS.toMinutes(currentTime);
                long diffHours = TimeUnit.MILLISECONDS.toHours(currentTime);
                if (diffMinutes <= 1 && diffHours == 0) {
                    return JUST_NOW;
                }
                if (diffMinutes <= 59 && diffHours == 0) {
                    return String.valueOf(diffMinutes) + MINUTES + AGO;
                }
                if (diffHours < 24) {
                    return String.valueOf(diffHours) + HOURS + AGO;
                }
            }
            if (isYesterday(timestamp)) {
                return YESTERDAY;
            }
            return simpleDateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public static boolean isYesterday(Long timestamp) {
        Calendar c1 = Calendar.getInstance();
        c1.add(Calendar.DAY_OF_YEAR, -1);
        Date date = new Date(timestamp);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(date);
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                && c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR);
    }

    public static Calendar getDatePart(Date date) {
        Calendar cal = Calendar.getInstance();       // get calendar instance
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);            // set hour to midnight
        cal.set(Calendar.MINUTE, 0);                 // set minute in hour
        cal.set(Calendar.SECOND, 0);                 // set second in minute
        cal.set(Calendar.MILLISECOND, 0);            // set millisecond in second

        return cal;                                  // return the date part
    }

    /**
     * This method also assumes endDate >= startDate
     **/
    public static long daysBetween(Date startDate, Date endDate) {
        Calendar sDate = getDatePart(startDate);
        Calendar eDate = getDatePart(endDate);

        long daysBetween = 0;
        while (sDate.before(eDate)) {
            sDate.add(Calendar.DAY_OF_MONTH, 1);
            daysBetween++;
        }
        return daysBetween;
    }

    public static String getDateAndTimeInDefaultFormat(long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, MMM dd,yyyy hh:mm aa");
        return simpleDateFormat.format(date);
    }


    /**
     * <p>
     * Simple SNTP client class for retrieving network time.
     * <p>
     * Sample usage:
     * <pre>SntpClient client = new SntpClient();
     * if (client.requestTime("time.foo.com")) {
     *     long now = client.getNtpTime() + SystemClock.elapsedRealtime() - client.getNtpTimeReference();
     * }
     * </pre>
     */
    private static class SntpClient {
        private static final String TAG = "SntpClient";

        private static final int REFERENCE_TIME_OFFSET = 16;
        private static final int ORIGINATE_TIME_OFFSET = 24;
        private static final int RECEIVE_TIME_OFFSET = 32;
        private static final int TRANSMIT_TIME_OFFSET = 40;
        private static final int NTP_PACKET_SIZE = 48;

        private static final int NTP_PORT = 123;
        private static final int NTP_MODE_CLIENT = 3;
        private static final int NTP_VERSION = 3;

        // Number of seconds between Jan 1, 1900 and Jan 1, 1970
        // 70 years plus 17 leap days
        private static final long OFFSET_1900_TO_1970 = ((365L * 70L) + 17L) * 24L * 60L * 60L;

        // system time computed from NTP server response
        private long mNtpTime;

        // value of SystemClock.elapsedRealtime() corresponding to mNtpTime
        private long mNtpTimeReference;

        // round trip time in milliseconds
        private long mRoundTripTime;

        /**
         * Sends an SNTP request to the given host and processes the response.
         *
         * @param host    host name of the server.
         * @param timeout network timeout in milliseconds.
         * @return true if the transaction was successful.
         */
        public boolean requestTime(String host, int timeout) {
            DatagramSocket socket = null;
            try {
                socket = new DatagramSocket();
                socket.setSoTimeout(timeout);
                InetAddress address = InetAddress.getByName(host);
                byte[] buffer = new byte[NTP_PACKET_SIZE];
                DatagramPacket request = new DatagramPacket(buffer, buffer.length, address, NTP_PORT);

                // set mode = 3 (client) and version = 3
                // mode is in low 3 bits of first byte
                // version is in bits 3-5 of first byte
                buffer[0] = NTP_MODE_CLIENT | (NTP_VERSION << 3);

                // get current time and write it to the request packet
                long requestTime = System.currentTimeMillis();
                long requestTicks = SystemClock.elapsedRealtime();
                writeTimeStamp(buffer, TRANSMIT_TIME_OFFSET, requestTime);

                socket.send(request);

                // read the response
                DatagramPacket response = new DatagramPacket(buffer, buffer.length);
                socket.receive(response);
                long responseTicks = SystemClock.elapsedRealtime();
                long responseTime = requestTime + (responseTicks - requestTicks);

                // extract the results
                long originateTime = readTimeStamp(buffer, ORIGINATE_TIME_OFFSET);
                long receiveTime = readTimeStamp(buffer, RECEIVE_TIME_OFFSET);
                long transmitTime = readTimeStamp(buffer, TRANSMIT_TIME_OFFSET);
                long roundTripTime = responseTicks - requestTicks - (transmitTime - receiveTime);
                // receiveTime = originateTime + transit + skew
                // responseTime = transmitTime + transit - skew
                // clockOffset = ((receiveTime - originateTime) + (transmitTime - responseTime))/2
                //             = ((originateTime + transit + skew - originateTime) +
                //                (transmitTime - (transmitTime + transit - skew)))/2
                //             = ((transit + skew) + (transmitTime - transmitTime - transit + skew))/2
                //             = (transit + skew - transit + skew)/2
                //             = (2 * skew)/2 = skew
                long clockOffset = ((receiveTime - originateTime) + (transmitTime - responseTime)) / 2;
                // if (false) Log.d(TAG, "round trip: " + roundTripTime + " ms");
                // if (false) Log.d(TAG, "clock offset: " + clockOffset + " ms");

                // save our results - use the times on this side of the network latency
                // (response rather than request time)
                mNtpTime = responseTime + clockOffset;
                mNtpTimeReference = responseTicks;
                mRoundTripTime = roundTripTime;
            } catch (Exception e) {
                if (false) Log.d(TAG, "request time failed: " + e);
                return false;
            } finally {
                if (socket != null) {
                    socket.close();
                }
            }

            return true;
        }

        /**
         * Returns the time computed from the NTP transaction.
         *
         * @return time value computed from NTP server response.
         */
        public long getNtpTime() {
            return mNtpTime;
        }

        /**
         * Returns the reference clock value (value of SystemClock.elapsedRealtime())
         * corresponding to the NTP time.
         *
         * @return reference clock corresponding to the NTP time.
         */
        public long getNtpTimeReference() {
            return mNtpTimeReference;
        }

        /**
         * Returns the round trip time of the NTP transaction
         *
         * @return round trip time in milliseconds.
         */
        public long getRoundTripTime() {
            return mRoundTripTime;
        }

        /**
         * Reads an unsigned 32 bit big endian number from the given offset in the buffer.
         */
        private long read32(byte[] buffer, int offset) {
            byte b0 = buffer[offset];
            byte b1 = buffer[offset + 1];
            byte b2 = buffer[offset + 2];
            byte b3 = buffer[offset + 3];

            // convert signed bytes to unsigned values
            int i0 = ((b0 & 0x80) == 0x80 ? (b0 & 0x7F) + 0x80 : b0);
            int i1 = ((b1 & 0x80) == 0x80 ? (b1 & 0x7F) + 0x80 : b1);
            int i2 = ((b2 & 0x80) == 0x80 ? (b2 & 0x7F) + 0x80 : b2);
            int i3 = ((b3 & 0x80) == 0x80 ? (b3 & 0x7F) + 0x80 : b3);

            return ((long) i0 << 24) + ((long) i1 << 16) + ((long) i2 << 8) + (long) i3;
        }

        /**
         * Reads the NTP time stamp at the given offset in the buffer and returns
         * it as a system time (milliseconds since January 1, 1970).
         */
        private long readTimeStamp(byte[] buffer, int offset) {
            long seconds = read32(buffer, offset);
            long fraction = read32(buffer, offset + 4);
            return ((seconds - OFFSET_1900_TO_1970) * 1000) + ((fraction * 1000L) / 0x100000000L);
        }

        /**
         * Writes system time (milliseconds since January 1, 1970) as an NTP time stamp
         * at the given offset in the buffer.
         */
        private void writeTimeStamp(byte[] buffer, int offset, long time) {
            long seconds = time / 1000L;
            long milliseconds = time - seconds * 1000L;
            seconds += OFFSET_1900_TO_1970;

            // write seconds in big endian format
            buffer[offset++] = (byte) (seconds >> 24);
            buffer[offset++] = (byte) (seconds >> 16);
            buffer[offset++] = (byte) (seconds >> 8);
            buffer[offset++] = (byte) (seconds >> 0);

            long fraction = milliseconds * 0x100000000L / 1000L;
            // write fraction in big endian format
            buffer[offset++] = (byte) (fraction >> 24);
            buffer[offset++] = (byte) (fraction >> 16);
            buffer[offset++] = (byte) (fraction >> 8);
            // low order bits should be random data
            buffer[offset++] = (byte) (Math.random() * 255.0);
        }
    }

}
