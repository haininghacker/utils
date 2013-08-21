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
 * 提供与编码、解码相关的工具方法。
 * 
 * @see com.iwallet.biz.common.util.AlipayCodec
 * @author haisheng.zhang
 * @version $Id: AlipayCodec.java, v 0.1 2011-12-21 上午10:39:46 haisheng.zhang Exp $
 */
public class AlipayCodec {
    /** logger */
    private static final Logger LOGGER                = LoggerFactory.getLogger(AlipayCodec.class);

    /** 密码杂凑种子 - 此项绝对不可更改，否则会造成密码验证失败 */
    private static final String PASSWORD_SEED         = "18nnad7f1njdy7f23nadifu23djfdu";

    // ----- 容器管理依赖 -----


    /** 15位身份证号模式 */
    private static Pattern             idCardNumberPattern15 = Pattern.compile("^([0-9]){17}[0-9Xx]{1}$");

    /** 18位身份证号模式 */
    private Pattern             idCardNumberPattern18 = Pattern.compile("^([0-9]){17}[0-9Xx]{1}$");

    /**
     * 对明文的密码进行杂凑。
     * 
     * @param clearPassword
     *            明文形式的密码。
     * 
     * @return 32位长度，包含0-9及abcdef的密码杂凑值。如果发生错误，则返回null。
     */
    public String encodePassword(String clearPassword) {
        return DigestUtils.md5Hex(PASSWORD_SEED + clearPassword);
    }

    
    public static void main(String[] args) {
        System.out.println((char)((int)(Math.random() * 36) - 10 + 'a'));
        System.out.println('a' + 0);
    }
    
    /**
     * 判断明文密码与杂凑密码是否相同。
     * 
     * @param clearPassword
     *            明文形式的密码。
     * @param encodedPassword
     *            杂凑形式的密码。
     * 
     * @return 如果明文密码与杂凑形式的密码匹配，则返回true，否则返回false。如果任一参数为null，则返回false。
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
     * 取自动生成的随机新密码确认码。
     * 
     * <p>
     * 自动生成的随机新密码确认码长度为6，其中只包括数字。
     * </p>
     * 
     * @return 自动生成的随机新密码确认码。
     */
    public String getNewPasswordAckNum() {
        return getRandomString(6, false);
    }

    /**
     * 取自动生成的随机新密码。
     * 
     * <p>
     * 自动生成的随机新密码长度为8，其中包括数字和小写字母。
     * </p>
     * 
     * @return 自动生成的随机新密码。
     */
    public String getNewPassword() {
        return getRandomString(8, true);
    }

    /**
     * 取自动随机生成的新邮件地址确认码。
     * 
     * <p>
     * 自动随机生成的新邮件地址确认码长度为6，其中只包括数字。
     * </p>
     * 
     * @return 自动随机生成的新邮件地址确认码。
     */
    public String getNewEmailAckNum() {
        return getRandomString(6, false);
    }

    /**
     * 获取一个随机字符串。
     * 
     * @param length
     *            随机字符串的长度
     * @param allowLetter
     *            是否允许使用字母
     * 
     * @return 一个随机字符串
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
     * 获取一个随机字符（允许数字0-9和小写字母）。
     * 
     * @return 一个随机字符
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
     * 获取一个随机字符（只允许数字0-9）。
     * 
     * @return 一个随机字符
     */
    protected char genRandomDigit(boolean allowZero) {
        int randomNumber = 0;

        do {
            randomNumber = (int) (Math.random() * 10);
        } while ((randomNumber == 0) && !allowZero);

        return (char) (randomNumber + '0');
    }

    /**
     * 将点分数字形式的IP地址编码成16进制形式。
     * 
     * <p>
     * 例如：点分数字形式的IP地址为<code>10.0.28.57</code>，则16进制形式的IP地址形式为<code>0a001c39</code>。
     * </p>
     * 
     * @param ip
     *            点分数字形式的IP地址
     * 
     * @return 16进制形式的IP地址。如果IP地址形式无效，则返回null。
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
     * 将身份证号规格化为18位。
     * 
     * @param idCardNumber
     *            15或18位的身份证号
     * 
     * @return 18位规格化的身份证号
     */
    public String getIdCardNumber18(String idCardNumber) {
        if (StringUtil.isEmpty(idCardNumber)) {
            return null;
        }
        /*
         * BUGFIX:18位身份证最后一位应该是X
         */
        if (idCardNumber.length() == 18) {
            return idCardNumber.toUpperCase();
        } else if (idCardNumber.length() != 15) {
            return null;
        }

        /* 首先将身份证号码扩展至17位: 将出生年扩展为19XX的形式 */
        String idCardNumber17 = idCardNumber.substring(0, 6) + "19" + idCardNumber.substring(6);

        /* 计算校验码 */
        int nSum = 0;

        try {
            for (int nCount = 0; nCount < 17; nCount++) {
                nSum += (Integer.parseInt(idCardNumber17.substring(nCount, nCount + 1)) * (Math
                    .pow(2, 17 - nCount) % 11));
            }
        } catch (Exception e) {
        	LOGGER.error("getIdCardNumber18方法出错",e);
        }

        nSum %= 11;

        if (nSum <= 1) {
            nSum = 1 - nSum;
        } else {
            nSum = 12 - nSum;
        }
        /*
         * BUGFIX:18位身份证最后一位应该是X
         */
        if (nSum == 10) {
            return idCardNumber17 + "X";
        } else {
            return idCardNumber17 += nSum;
        }
    }

    /**
     * 将身份证号规格化为15位。
     * 
     * @param idCardNumber
     *            15或18位的身份证号
     * 
     * @return 15位规格化的身份证号
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
     * 判断两个身份证号码是否一致，可以屏蔽15＆18区分，直接可以判断身份证是否一致
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
     * 从身份证号析取年龄。
     * 
     * @param idCardNumber
     *            15或18位的身份证号
     * 
     * @return 在身份证中析取出的年龄
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
     * 检查身份证号是否有效。
     * 
     * @param idCardNumber
     * @return
     */
    public boolean checkIdCardNumber(String idCardNumber) {
        return checkIdCardNumber(idCardNumber, -1);
    }

    /**
     * 根据身份证号码判断是不是男性，在这个方法之前一点要确定传入的号码一定是身份证号码
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
     * 检查身份证号是否有效。
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
     * 检查日期是否有效。
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
     * 取得当前年份。
     * 
     * @return
     */
    protected int getCurrentYear() {
        Calendar cal = new GregorianCalendar();

        cal.setTime(new Date());
        return cal.get(Calendar.YEAR);
    }

    /**
     * 计算校验码。
     * 
     * <p>
     * 校验码规则: 1-15位做异或，并用10取模。
     * 
     * @param string
     * @return
     */
    public int computeChecksum(String string) {
        // 计算校验码
        int checksum = 0;

        // 计算校验和
        for (int i = 0; i < string.length(); i++) {
            checksum ^= (string.charAt(i) - '0');
        }

        return checksum % 10;
    }

    /**
     * 根据指定序列号生成账务订单号
     * 
     * @param seq
     * @return
     */
    public String genAccountOrderNo(long seq) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("BO");

        // 8位日期
        buffer.append((new SimpleDateFormat("yyyyMMdd")).format(new Date()));

        // 8位流水号
        buffer.append(StringUtil.alignRight(String.valueOf(seq), 8, "0"));

        return buffer.toString();

    }

    /**
     * 根据指定序列号生成交易订单号
     * 
     * @param seq
     * @return
     */
    public String genTradeOrderNo(long seq) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("TO");

        // 8位日期
        buffer.append((new SimpleDateFormat("yyyyMMdd")).format(new Date()));

        // 8位流水号
        buffer.append(StringUtil.alignRight(String.valueOf(seq), 8, "0"));

        return buffer.toString();

    }

    /**
     * 根据指定序列号生成交易号。
     * 
     * @param seq
     * @return
     */
    public String genTradeNo(long seq) {
        StringBuffer buffer = new StringBuffer();

        // 8位日期
        buffer.append((new SimpleDateFormat("yyyyMMdd")).format(new Date()));

        // 8位流水号。如果seq超过8位，取最右边8位；否则用0充足8位
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
     * 根据指定序列号生成批量打款批次号
     * 
     * @param seq
     * @return
     */
    public String genBatchNo(long seq) {
        StringBuffer buffer = new StringBuffer();

        // 8位日期
        buffer.append((new SimpleDateFormat("yyyyMMdd")).format(new Date()));

        // 6位流水号
        buffer.append(StringUtil.alignRight(String.valueOf(seq), 6, "0"));

        return buffer.toString();
    }

    /**
     * 根据指定序列号生成贷款信息号
     * 
     * @param seq
     * @return
     */
    public String genLoanId(long seq) {
        StringBuffer buffer = new StringBuffer();

        // 8位日期
        buffer.append((new SimpleDateFormat("yyyyMMdd")).format(new Date()));

        // 12位流水号。如果seq超过12位，取最右边12位；否则用0充足12位
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
     * 对字符串进行mask，保持其长度不变，但当中的字符使用*号代替。
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