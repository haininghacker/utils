/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2009 All Rights Reserved.
 */
package com.alipay.wapcashier.common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.digest.DigestUtils;

import com.alibaba.common.lang.StringUtil;
import com.alibaba.common.logging.Logger;
import com.alibaba.common.logging.LoggerFactory;


/**
 * �ṩ����롢������صĹ��߷�����
 * 
 * @see com.iwallet.biz.common.util.AlipayCodec
 * @author haisheng.zhang
 * @version $Id: AlipayCodec.java, v 0.1 2011-12-21 ����10:39:46 haisheng.zhang Exp $
 */
public class AlipayCodec {
    /** logger */
    private static final Logger LOGGER                = LoggerFactory.getLogger(AlipayCodec.class);

    /** �����Ӵ����� - ������Բ��ɸ��ģ���������������֤ʧ�� */
    private static final String PASSWORD_SEED         = "18nnad7f1njdy7f23nadifu23djfdu";

    // ----- ������������ -----


    /** 15λ���֤��ģʽ */
    private static Pattern             idCardNumberPattern15 = Pattern.compile("^([0-9]){17}[0-9Xx]{1}$");

    /** 18λ���֤��ģʽ */
    private Pattern             idCardNumberPattern18 = Pattern.compile("^([0-9]){17}[0-9Xx]{1}$");

    /**
     * �����ĵ���������Ӵա�
     * 
     * @param clearPassword
     *            ������ʽ�����롣
     * 
     * @return 32λ���ȣ�����0-9��abcdef�������Ӵ�ֵ��������������򷵻�null��
     */
    public String encodePassword(String clearPassword) {
        return DigestUtils.md5Hex(PASSWORD_SEED + clearPassword);
    }

    
    public static void main(String[] args) {
        System.out.println((char)((int)(Math.random() * 36) - 10 + 'a'));
        System.out.println('a' + 0);
    }
    
    /**
     * �ж������������Ӵ������Ƿ���ͬ��
     * 
     * @param clearPassword
     *            ������ʽ�����롣
     * @param encodedPassword
     *            �Ӵ���ʽ�����롣
     * 
     * @return ��������������Ӵ���ʽ������ƥ�䣬�򷵻�true�����򷵻�false�������һ����Ϊnull���򷵻�false��
     */
    public boolean isPasswordEqual(String clearPassword, String encodedPassword) {
        if ((encodedPassword == null) || (clearPassword == null)) {
            return false;
        }

        if (encodedPassword.equals(encodePassword(clearPassword))) {
            return true;
        }

        return false;
    }


    /**
     * ȡ�Զ����ɵ����������ȷ���롣
     * 
     * <p>
     * �Զ����ɵ����������ȷ���볤��Ϊ6������ֻ�������֡�
     * </p>
     * 
     * @return �Զ����ɵ����������ȷ���롣
     */
    public String getNewPasswordAckNum() {
        return getRandomString(6, false);
    }

    /**
     * ȡ�Զ����ɵ���������롣
     * 
     * <p>
     * �Զ����ɵ���������볤��Ϊ8�����а������ֺ�Сд��ĸ��
     * </p>
     * 
     * @return �Զ����ɵ���������롣
     */
    public String getNewPassword() {
        return getRandomString(8, true);
    }

    /**
     * ȡ�Զ�������ɵ����ʼ���ַȷ���롣
     * 
     * <p>
     * �Զ�������ɵ����ʼ���ַȷ���볤��Ϊ6������ֻ�������֡�
     * </p>
     * 
     * @return �Զ�������ɵ����ʼ���ַȷ���롣
     */
    public String getNewEmailAckNum() {
        return getRandomString(6, false);
    }

    /**
     * ��ȡһ������ַ�����
     * 
     * @param length
     *            ����ַ����ĳ���
     * @param allowLetter
     *            �Ƿ�����ʹ����ĸ
     * 
     * @return һ������ַ���
     */
    public String getRandomString(int length, boolean allowLetter) {
        if (length < 1) {
            return "";
        }

        StringBuffer sb = new StringBuffer(length);

        for (int i = 0; i < length; i++) {
            sb.append(allowLetter ? genRandomChar(i != 0) : genRandomDigit(i != 0));
        }

        return sb.toString();
    }

    /**
     * ��ȡһ������ַ�����������0-9��Сд��ĸ����
     * 
     * @return һ������ַ�
     */
    protected char genRandomChar(boolean allowZero) {
        int randomNumber = 0;

        do {
            randomNumber = (int) (Math.random() * 36);
        } while ((randomNumber == 0) && !allowZero);

        if (randomNumber < 10) {
            return (char) (randomNumber + '0');
        } else {
            return (char) (randomNumber - 10 + 'a');
        }
    }

    /**
     * ��ȡһ������ַ���ֻ��������0-9����
     * 
     * @return һ������ַ�
     */
    protected char genRandomDigit(boolean allowZero) {
        int randomNumber = 0;

        do {
            randomNumber = (int) (Math.random() * 10);
        } while ((randomNumber == 0) && !allowZero);

        return (char) (randomNumber + '0');
    }

    /**
     * �����������ʽ��IP��ַ�����16������ʽ��
     * 
     * <p>
     * ���磺���������ʽ��IP��ַΪ<code>10.0.28.57</code>����16������ʽ��IP��ַ��ʽΪ<code>0a001c39</code>��
     * </p>
     * 
     * @param ip
     *            ���������ʽ��IP��ַ
     * 
     * @return 16������ʽ��IP��ַ�����IP��ַ��ʽ��Ч���򷵻�null��
     */
    public String encodeIP(String ip) {
        if (StringUtil.isEmpty(ip)) {
            return null;
        }

        String[] ipParts = ip.split("\\.");

        if (ipParts.length != 4) {
            LOGGER.error("IP format is invalid: " + ip);
            return null;
        }

        String encodedIP = "";

        try {
            for (int i = 0; i < 4; i++) {
                int ipPart = Integer.parseInt(ipParts[i]);
                String hex = Integer.toHexString(ipPart);

                if (hex.length() == 1) {
                    hex = "0" + hex;
                }

                encodedIP += hex;
            }
        } catch (Exception e) {
            LOGGER.error("Failed to parse IP address due to exception.", e);
            return null;
        }

        return encodedIP;
    }

    /**
     * �����֤�Ź��Ϊ18λ��
     * 
     * @param idCardNumber
     *            15��18λ�����֤��
     * 
     * @return 18λ��񻯵����֤��
     */
    public String getIdCardNumber18(String idCardNumber) {
        if (StringUtil.isEmpty(idCardNumber)) {
            return null;
        }
        /*
         * BUGFIX:18λ���֤���һλӦ����X
         */
        if (idCardNumber.length() == 18) {
            return idCardNumber.toUpperCase();
        } else if (idCardNumber.length() != 15) {
            return null;
        }

        /* ���Ƚ����֤������չ��17λ: ����������չΪ19XX����ʽ */
        String idCardNumber17 = idCardNumber.substring(0, 6) + "19" + idCardNumber.substring(6);

        /* ����У���� */
        int nSum = 0;

        try {
            for (int nCount = 0; nCount < 17; nCount++) {
                nSum += (Integer.parseInt(idCardNumber17.substring(nCount, nCount + 1)) * (Math
                    .pow(2, 17 - nCount) % 11));
            }
        } catch (Exception e) {
        	LOGGER.error("getIdCardNumber18��������",e);
        }

        nSum %= 11;

        if (nSum <= 1) {
            nSum = 1 - nSum;
        } else {
            nSum = 12 - nSum;
        }
        /*
         * BUGFIX:18λ���֤���һλӦ����X
         */
        if (nSum == 10) {
            return idCardNumber17 + "X";
        } else {
            return idCardNumber17 += nSum;
        }
    }

    /**
     * �����֤�Ź��Ϊ15λ��
     * 
     * @param idCardNumber
     *            15��18λ�����֤��
     * 
     * @return 15λ��񻯵����֤��
     */
    public String getIdCardNumber15(String idCardNumber) {
        if (StringUtil.isEmpty(idCardNumber)) {
            return null;
        }

        if (idCardNumber.length() == 15) {
            return idCardNumber;
        } else if (idCardNumber.length() == 18) {
            return idCardNumber.substring(0, 6) + idCardNumber.substring(8, 17);
        }

        return null;
    }

    /**
     * �ж��������֤�����Ƿ�һ�£���������15��18���֣�ֱ�ӿ����ж����֤�Ƿ�һ��
     * 
     *
     * @param newCertNo
     * @param oldCertNo
     * @return
     */
    public boolean checkCertNoEquals(String newCertNo, String oldCertNo) {
        if (StringUtil.isBlank(oldCertNo) || StringUtil.isBlank(newCertNo)) {
            return false;
        }

        if (!checkIdCardNumber(newCertNo) || !checkIdCardNumber(oldCertNo)) {
            return false;
        }

        if (StringUtil.equalsIgnoreCase(oldCertNo, getIdCardNumber15(newCertNo))
            || StringUtil.equalsIgnoreCase(oldCertNo, getIdCardNumber18(newCertNo))) {
            return true;
        }

        return false;
    }

    /**
     * �����֤����ȡ���䡣
     * 
     * @param idCardNumber
     *            15��18λ�����֤��
     * 
     * @return �����֤����ȡ��������
     */
    public int getAgeFromIdCardNumber(String idCardNumber) {
        if (StringUtil.isEmpty(idCardNumber)) {
            return 0;
        }

        String strYear = null;

        if (idCardNumber.length() == 15) {
            strYear = "19" + idCardNumber.substring(6, 8);
        } else if (idCardNumber.length() == 18) {
            strYear = idCardNumber.substring(6, 10);
        } else {
            return 0;
        }

        int year = 0;

        try {
            year = Integer.parseInt(strYear);
        } catch (Exception e) {
            LOGGER.error("Failed to parse year from idCardNumber " + idCardNumber
                         + " due to exception.", e);
            return 0;
        }

        int thisYear = (new GregorianCalendar()).get(Calendar.YEAR);

        return (thisYear - year);
    }

    /**
     * ������֤���Ƿ���Ч��
     * 
     * @param idCardNumber
     * @return
     */
    public boolean checkIdCardNumber(String idCardNumber) {
        return checkIdCardNumber(idCardNumber, -1);
    }

    /**
     * �������֤�����ж��ǲ������ԣ����������֮ǰһ��Ҫȷ������ĺ���һ�������֤����
     * @param idCardNumber
     * @return
     */
    public boolean checkIsGenderManByIdCardNumber(String idCardNumber) {
        int n = 0;
        if (StringUtil.isBlank(idCardNumber)) {
            return false;
        }
        if (idCardNumber.length() == 18) {
            n = Integer.parseInt(StringUtil.substring(idCardNumber, 16, 17));
        } else {
            n = Integer.parseInt(StringUtil.substring(idCardNumber, 14, 15));
        }

        return n % 2 == 1;
    }

    /**
     * ������֤���Ƿ���Ч��
     * 
     * @param idCardNumber
     * @param age
     * @return
     */
    public boolean checkIdCardNumber(String idCardNumber, int age) {
        Matcher matcher = null;

        matcher = idCardNumberPattern15.matcher(idCardNumber);

        if (!matcher.find()) {
            matcher = idCardNumberPattern18.matcher(idCardNumber);

            if (!matcher.find()) {
                return false;
            }
        }

        String idCardNumber18 = getIdCardNumber18(idCardNumber);

        if (idCardNumber18 == null) {
            return false;
        }

        try {
            int year = Integer.parseInt(idCardNumber18.substring(6, 10));
            int month = Integer.parseInt(idCardNumber18.substring(10, 12));
            int day = Integer.parseInt(idCardNumber18.substring(12, 14));

            if (!checkDate(year, month, day)) {
                return false;
            }

            if (age > (getCurrentYear() - year)) {
                return false;
            }
        } catch (Exception e) {
            LOGGER.warn("Failed to parse integer due to exception.", e);
            return false;
        }

        if (idCardNumber.length() == 18) {
            if (!idCardNumber.equalsIgnoreCase(getIdCardNumber18(getIdCardNumber15(idCardNumber)))) {
                return false;
            }
        }

        return true;
    }

    /**
     * ��������Ƿ���Ч��
     * 
     * @param year
     * @param month
     * @param day
     * @return
     */
    public boolean checkDate(int year, int month, int day) {
        if ((year < 1900) || (year > getCurrentYear())) {
            return false;
        }

        if ((month < 1) || (month > 12)) {
            return false;
        }

        Calendar cal = new GregorianCalendar();

        cal.set(year, month - 1, 1);

        if ((day < 1) || (day > cal.getActualMaximum(Calendar.DAY_OF_MONTH))) {
            return false;
        }

        return true;
    }

    /**
     * ȡ�õ�ǰ��ݡ�
     * 
     * @return
     */
    protected int getCurrentYear() {
        Calendar cal = new GregorianCalendar();

        cal.setTime(new Date());
        return cal.get(Calendar.YEAR);
    }

    /**
     * ����У���롣
     * 
     * <p>
     * У�������: 1-15λ����򣬲���10ȡģ��
     * 
     * @param string
     * @return
     */
    public int computeChecksum(String string) {
        // ����У����
        int checksum = 0;

        // ����У���
        for (int i = 0; i < string.length(); i++) {
            checksum ^= (string.charAt(i) - '0');
        }

        return checksum % 10;
    }

    /**
     * ����ָ�����к��������񶩵���
     * 
     * @param seq
     * @return
     */
    public String genAccountOrderNo(long seq) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("BO");

        // 8λ����
        buffer.append((new SimpleDateFormat("yyyyMMdd")).format(new Date()));

        // 8λ��ˮ��
        buffer.append(StringUtil.alignRight(String.valueOf(seq), 8, "0"));

        return buffer.toString();

    }

    /**
     * ����ָ�����к����ɽ��׶�����
     * 
     * @param seq
     * @return
     */
    public String genTradeOrderNo(long seq) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("TO");

        // 8λ����
        buffer.append((new SimpleDateFormat("yyyyMMdd")).format(new Date()));

        // 8λ��ˮ��
        buffer.append(StringUtil.alignRight(String.valueOf(seq), 8, "0"));

        return buffer.toString();

    }

    /**
     * ����ָ�����к����ɽ��׺š�
     * 
     * @param seq
     * @return
     */
    public String genTradeNo(long seq) {
        StringBuffer buffer = new StringBuffer();

        // 8λ����
        buffer.append((new SimpleDateFormat("yyyyMMdd")).format(new Date()));

        // 8λ��ˮ�š����seq����8λ��ȡ���ұ�8λ��������0����8λ
        int noBits = 8;
        String seqStr = String.valueOf(seq);
        if (seqStr.length() <= noBits) {
            buffer.append(StringUtil.alignRight(seqStr, noBits, "0"));
        } else {
            buffer.append(StringUtil.right(seqStr, noBits));
        }

        return buffer.toString();
    }

    /**
     * ����ָ�����к���������������κ�
     * 
     * @param seq
     * @return
     */
    public String genBatchNo(long seq) {
        StringBuffer buffer = new StringBuffer();

        // 8λ����
        buffer.append((new SimpleDateFormat("yyyyMMdd")).format(new Date()));

        // 6λ��ˮ��
        buffer.append(StringUtil.alignRight(String.valueOf(seq), 6, "0"));

        return buffer.toString();
    }

    /**
     * ����ָ�����к����ɴ�����Ϣ��
     * 
     * @param seq
     * @return
     */
    public String genLoanId(long seq) {
        StringBuffer buffer = new StringBuffer();

        // 8λ����
        buffer.append((new SimpleDateFormat("yyyyMMdd")).format(new Date()));

        // 12λ��ˮ�š����seq����12λ��ȡ���ұ�12λ��������0����12λ
        int noBits = 12;
        String seqStr = String.valueOf(seq);
        if (seqStr.length() <= noBits) {
            buffer.append(StringUtil.alignRight(seqStr, noBits, "0"));
        } else {
            buffer.append(StringUtil.right(seqStr, noBits));
        }

        return buffer.toString();
    }

    /**
     * ���ַ�������mask�������䳤�Ȳ��䣬�����е��ַ�ʹ��*�Ŵ��档
     * 
     * @param text
     * @return
     */
    public String maskText(String text) {
        if (text == null) {
            return null;
        }

        if (text.length() <= 2) {
            return StringUtil.repeat("*", text.length());
        }

        return text.charAt(0) + StringUtil.repeat("*", text.length() - 2)
               + text.charAt(text.length() - 1);
    }


    public String bytes2Hex(byte[] bts) {
        String des = "";
        String tmp = null;
        for (int i = 0; i < bts.length; i++) {
            tmp = (Integer.toHexString(bts[i] & 0xFF));
            if (tmp.length() == 1) {
                des += "0";
            }
            des += tmp;
        }
        return des;
    }



}