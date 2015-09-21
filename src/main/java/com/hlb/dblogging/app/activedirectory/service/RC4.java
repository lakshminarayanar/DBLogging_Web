package com.hlb.dblogging.app.activedirectory.service;

/*
 * Created on 14-Dec-2006
 *
 
*/
import java.io.*;

/**
 * <code>RC4</code> is a cipher algorithm initially implemented in C++ by Ron Rivest (RSA).
 * Symmentric cipher key is used for both encryption and decryption.
 * @author  Jiang Jin Hui
 * @version 1.0, 23/06/01
 */
public class RC4 {
    int S[] = new int[256];
    int _S[] = new int[256];
    int i, j, t;
    boolean test = false;
    boolean done = false;
    boolean initialized = false;

    public RC4() {
    }
    public void setKey(String key) {
        init(key, key.length());
    }

    public void init(String key, int keylen) {
        int keypos = 0, x;
        int K[] = new int[256];
        if (keylen > 256) return;
        try {
            for (x = 0; x < 256; x++) {
                S[x] = (int)x;
                char k = key.charAt(keypos);
                K[x] = (int)(k);
                keypos++;
                if (keypos >= keylen) keypos = 0;
            }
            j = 0;
            for (x = 0; x < 256; x++) {
                j += S[x] + K[x];
                j = j % 256;
                t = S[x];
                S[x] = S[j];
                S[j] = t;
            }
            i = j = 0;
            initialized = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // used by the encryption or decryption function
    // the value returned, had to be XORed with the plaintext or the
    // ciphertext to encrypt or decrypt.
    int next_rand() {
        ++i;
        i = i % 256;
        j += S[i];
        j = j % 256;
        t = S[i]; S[i] = S[j]; S[j] = t;
        t = S[i] + S[j];
        return S[t % 256];
    }

    void save() {
        System.arraycopy(S, 0, _S, 0, S.length);
        done = true;
    }

    void restore() {
        if (done) {
            System.arraycopy(_S, 0, S, 0, S.length);
            i = j = 0;
        }
    }

    public byte[] encrypt(byte text[]) throws Exception {
        if (initialized == false) throw new Exception("Not initialized");
        save();
        ByteArrayInputStream sr = new ByteArrayInputStream(text);
        ByteArrayOutputStream sb = new ByteArrayOutputStream();
        int c;
        int ii = 0;
        try {
            while ((c = sr.read()) != -1) {
                int nr = next_rand();
                sb.write((c ^ nr));
            }
            sb.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        restore();
        //return sb.toString();
        return sb.toByteArray();
    }


    public void encrypt(File in, File out) throws Exception {
        if (initialized == false) throw new Exception("Not initialized");
        save();
        FileInputStream sr = new FileInputStream(in);
        FileOutputStream sw = new FileOutputStream(out);
        int c;
        int ii = 0;
        try {
            while ((c = sr.read()) != -1) {
                int nr = next_rand();
                sw.write(c ^ nr);
            }
            sw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        restore();
    }

    public void decrypt(File in, File out) throws Exception {
        encrypt(in, out);
    }

    public String encrypt(String sPlainText) {
        try {
            byte[] bb = sPlainText.getBytes();
            byte[] bb2 = encrypt(bb);
            String s = hexdump(bb2);
            return s;
        } catch (Exception e) {
            return sPlainText;
        }
    }

    public String decrypt(String cipherText) {
        try {
            byte[] bb = bytedump(cipherText);
            byte[] bb2 = encrypt(bb);
            return new String(bb2);
        } catch (Exception e) {
            return cipherText;
        }
    }

    String hexdump(byte[] todump) {
        String sHexTab = "0123456789abcdef";
        String sDump = "";
        char[] dumpbuf = new char[todump.length << 1];
        int nPos = 0;
        for (int nI = 0; nI < todump.length; nI++) {
            byte bAct = todump[nI];
            dumpbuf[nPos++] = sHexTab.charAt((bAct >> 4) & 0x0f);
            dumpbuf[nPos++] = sHexTab.charAt(bAct & 0x0f);
        };
        return new String(dumpbuf);
    }

    byte[] bytedump(String hexStr) {
        String sHexTab = "0123456789abcdef";
        String sDump = "";
        byte[] dumpbuf = new byte[hexStr.length() >> 1];
        int nI, nPos = 0;
        int nLen = dumpbuf.length;
        for (nI = 0; nI < nLen; nI++) {
            byte bActByte = 0;
            for (int nJ = 0; nJ < 2; nJ++) {
                bActByte <<= 4;
                char cActChar = hexStr.charAt(nPos++);
                int c = sHexTab.indexOf(cActChar);
                bActByte |= (byte)(c);
            };
            dumpbuf[nI] = bActByte;
        };
        return dumpbuf;
    }
    public static void main(String argv[]) throws Exception {
        RC4 rc4 = new RC4();
        String key = "123456789abcdefghi";
        rc4.setKey(key);
        File f = new File("E:/temp/request.xml");
        FileInputStream fs = new FileInputStream(f);
        byte bs[] = new byte[80000];
        int n = fs.read(bs);
        String t1 = (new String(bs)).substring(0, n);

        String t2 = rc4.encrypt(t1);
        System.out.println("Encryption.....");
        //        System.out.println(new String(rc4.decrypt(rc4.encrypt(t1))));
        System.out.println(t2);
        System.out.println("Decryption.....");
        System.out.println(rc4.decrypt(t2));
        key = "abcdefghi123456789";
        rc4.setKey(key);
        t2 = rc4.encrypt(t1);
        System.out.println("Encryption.....");
        System.out.println(t2);
        System.out.println("Decryption.....");
        System.out.println(rc4.decrypt(t2));
    }
}
