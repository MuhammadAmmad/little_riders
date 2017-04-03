/*************** Base64Utility.java ********************/
package com.craftsilicon.littlecabrider.utils;
import java.io.UnsupportedEncodingException;

import android.util.Base64OutputStream;
 
/**
 * Utilities for encoding and decoding the Base64 representation of
 * binary data. See RFCs <a
 * href="http://www.ietf.org/rfc/rfc2045.txt">2045</a> and <a
 * href="http://www.ietf.org/rfc/rfc3548.txt">3548</a>.
 */
public class B6U {
 /**
 * Default values for encoder/decoder flags.
 */
 public static final int DEFAULT = 0;
 
/**
 * Encoder flag bit to omit the padding '=' characters at the end
 * of the output (if any).
 */
 public static final int NO_PADDING = 1;
 
/**
 * Encoder flag bit to omit all line terminators (i.e., the output
 * will be on one long line).
 */
 public static final int NO_WRAP = 2;
 
/**
 * Encoder flag bit to indicate lines should be terminated with a
 * CRLF pair instead of just an LF. Has no effect if {@code
 * NO_WRAP} is specified as well.
 */
 public static final int CRLF = 4;
 
/**
 * Encoder/decoder flag bit to indicate using the "URL and
 * filename safe" variant of Base64 (see RFC 3548 section 4) where
 * {@code -} and {@code _} are used in place of {@code +} and
 * {@code /}.
 */
 public static final int URL_SAFE = 8;
 
/**
 * Flag to pass to {@link Base64OutputStream} to indicate that it
 * should not close the output stream it is wrapping when it
 * itself is closed.
 */
 public static final int NO_CLOSE = 16;
 
static abstract class Coder {
 public byte[] output;
 public int op;
 
/**
 * Encode/decode another block of input data. this.output is
 * provided by the caller, and must be big enough to hold all
 * the coded data. On exit, this.opwill be set to the length
 * of the coded data.
 *
 * @param finish true if this is the final call to process for
 * this object. Will finalize the coder state and
 * include any final bytes in the output.
 *
 * @return true if the input so far is good; false if some
 * error has been detected in the input stream..
 */
 public abstract boolean process(byte[] input, int offset, int len, boolean finish);
 
/**
 * @return the maximum number of bytes a call to process()
 * could produce for the given number of input bytes. This may
 * be an overestimate.
 */
 public abstract int maxOutputSize(int len);
 }
 
/**
 * Decode the Base64-encoded data in input and return the data in
 * a new byte array.
 *
 * The padding '=' characters at the end are considered optional, but
 * if any are present, there must be the correct number of them.
 *
 * @param str the input String to decode, which is converted to
 * bytes using the default charset
 * @param flags controls certain features of the decoded output.
 * Pass {@code DEFAULT} to decode standard Base64.
 *
 * @throws IllegalArgumentException if the input contains
 * incorrect padding
 */
 public static byte[] decode(String str, int flags) {
 return decode(str.getBytes(), flags);
 }
 
/**
 * Decode the Base64-encoded data in input and return the data in
 * a new byte array.
 *
 * The padding '=' characters at the end are considered optional, but
 * if any are present, there must be the correct number of them.
 *
 * @param input the input array to decode
 * @param flags controls certain features of the decoded output.
 * Pass {@code DEFAULT} to decode standard Base64.
 *
 * @throws IllegalArgumentException if the input contains
 * incorrect padding
 */
 public static byte[] decode(byte[] input, int flags) {
 return decode(input, 0, input.length, flags);
 }
 
/**
 * Decode the Base64-encoded data in input and return the data in
 * a new byte array.
 *
 * The padding '=' characters at the end are considered optional, but
 * if any are present, there must be the correct number of them.
 *
 * @param input the data to decode
 * @param offset the position within the input array at which to start
 * @param len the number of bytes of input to decode
 * @param flags controls certain features of the decoded output.
 * Pass {@code DEFAULT} to decode standard Base64.
 *
 * @throws IllegalArgumentException if the input contains
 * incorrect padding
 */
 public static byte[] decode(byte[] input, int offset, int len, int flags) {
 Decoder decoder = new Decoder(flags, new byte[len*3/4]);
 
if (!decoder.process(input, offset, len, true)) {
 throw new IllegalArgumentException("bad base-64");
 }
 
if (decoder.op == decoder.output.length) {
 return decoder.output;
 }
 
byte[] temp = new byte[decoder.op];
 System.arraycopy(decoder.output, 0, temp, 0, decoder.op);
 return temp;
 }
 
static class Decoder extends Coder {
 /**
 * Lookup table for turning bytes into their position in the
 * Base64 alphabet.
 */
 private static final int DECODE[] = {
 -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
 -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
 -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63,
 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -2, -1, -1,
 -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14,
 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1,
 -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1,
 -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
 -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
 -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
 -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
 -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
 -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
 -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
 -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
 };
 
/**
 * Decode lookup table for the "web safe" variant (RFC 3548
 * sec. 4) where - and _ replace + and /.
 */
 private static final int DECODE_WEBSAFE[] = {
 -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
 -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
 -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1,
 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -2, -1, -1,
 -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14,
 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, 63,
 -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1,
 -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
 -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
 -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
 -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
 -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
 -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
 -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
 -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
 };
 
/** Non-data values in the DECODE arrays. */
 private static final int SKIP = -1;
 private static final int EQUALS = -2;
 
/**
 * States 0-3 are reading through the next input tuple.
 * State 4 is having read one '=' and expecting exactly
 * one more.
 * State 5 is expecting no more data or padding characters
 * in the input.
 * State 6 is the error state; an error has been detected
 * in the input and no future input can "fix" it.
 */
 private int state;
 private int value;
 
final private int[] alphabet;
 
public Decoder(int flags, byte[] output) {
 this.output = output;
 
alphabet = ((flags & URL_SAFE) == 0) ? DECODE : DECODE_WEBSAFE;
 state = 0;
 value = 0;
 }
 
/**
 * @return an overestimate for the number of bytes {@code
 * len} bytes could decode to.
 */
 @Override
public int maxOutputSize(int len) {
 return len * 3/4 + 10;
 }
 
/**
 * Decode another block of input data.
 *
 * @return true if the state machine is still healthy. false if
 * bad base-64 data has been detected in the input stream.
 */
 @Override
public boolean process(byte[] input, int offset, int len, boolean finish) {
 if (this.state == 6) return false;
 
int p = offset;
 len += offset;
 
int state = this.state;
 int value = this.value;
 int op = 0;
 final byte[] output = this.output;
 final int[] alphabet = this.alphabet;
 
while (p < len) {
 
 if (state == 0) {
 while (p+4 <= len &&
 (value = ((alphabet[input[p] & 0xff] << 18) |
 (alphabet[input[p+1] & 0xff] << 12) |
 (alphabet[input[p+2] & 0xff] << 6) |
 (alphabet[input[p+3] & 0xff]))) >= 0) {
 output[op+2] = (byte) value;
 output[op+1] = (byte) (value >> 8);
 output[op] = (byte) (value >> 16);
 op += 3;
 p += 4;
 }
 if (p >= len) break;
 }
 
int d = alphabet[input[p++] & 0xff];
 
switch (state) {
 case 0:
 if (d >= 0) {
 value = d;
 ++state;
 } else if (d != SKIP) {
 this.state = 6;
 return false;
 }
 break;
 
case 1:
 if (d >= 0) {
 value = (value << 6) | d;
 ++state;
 } else if (d != SKIP) {
 this.state = 6;
 return false;
 }
 break;
 
case 2:
 if (d >= 0) {
 value = (value << 6) | d;
 ++state;
 } else if (d == EQUALS) {
 output[op++] = (byte) (value >> 4);
 state = 4;
 } else if (d != SKIP) {
 this.state = 6;
 return false;
 }
 break;
 
case 3:
 if (d >= 0) {
 value = (value << 6) | d;
 output[op+2] = (byte) value;
 output[op+1] = (byte) (value >> 8);
 output[op] = (byte) (value >> 16);
 op += 3;
 state = 0;
 } else if (d == EQUALS) {
 output[op+1] = (byte) (value >> 2);
 output[op] = (byte) (value >> 10);
 op += 2;
 state = 5;
 } else if (d != SKIP) {
 this.state = 6;
 return false;
 }
 break;
 
case 4:
 if (d == EQUALS) {
 ++state;
 } else if (d != SKIP) {
 this.state = 6;
 return false;
 }
 break;
 
case 5:
 if (d != SKIP) {
 this.state = 6;
 return false;
 }
 break;
 }
 }
 
if (!finish) {
 this.state = state;
 this.value = value;
 this.op = op;
 return true;
 }
 
switch (state) {
 case 0:
 break;
 case 1:
 this.state = 6;
 return false;
 case 2:
 output[op++] = (byte) (value >> 4);
 break;
 case 3:
 output[op++] = (byte) (value >> 10);
 output[op++] = (byte) (value >> 2);
 break;
 case 4:
 this.state = 6;
 return false;
 case 5:
 break;
 }
 
this.state = state;
 this.op = op;
 return true;
 }
 }
 
/**
 * Base64-encode the given data and return a newly allocated
 * String with the result.
 *
 * @param input the data to encode
 * @param flags controls certain features of the encoded output.
 * Passing {@code DEFAULT} results in output that
 * adheres to RFC 2045.
 */
 public static String encodeToString(byte[] input, int flags) {
 try {
 return new String(encode(input, flags), "US-ASCII");
 } catch (UnsupportedEncodingException e) {
 throw new AssertionError(e);
 }
 }
 
/**
 * Base64-encode the given data and return a newly allocated
 * String with the result.
 *
 * @param input the data to encode
 * @param offset the position within the input array at which to
 * start
 * @param len the number of bytes of input to encode
 * @param flags controls certain features of the encoded output.
 * Passing {@code DEFAULT} results in output that
 * adheres to RFC 2045.
 */
 public static String encodeToString(byte[] input, int offset, int len, int flags) {
 try {
 return new String(encode(input, offset, len, flags), "US-ASCII");
 } catch (UnsupportedEncodingException e) {
 throw new AssertionError(e);
 }
 }
 
/**
 * Base64-encode the given data and return a newly allocated
 * byte[] with the result.
 *
 * @param input the data to encode
 * @param flags controls certain features of the encoded output.
 * Passing {@code DEFAULT} results in output that
 * adheres to RFC 2045.
 */
 public static byte[] encode(byte[] input, int flags) {
 return encode(input, 0, input.length, flags);
 }
 
/**
 * Base64-encode the given data and return a newly allocated
 * byte[] with the result.
 *
 * @param input the data to encode
 * @param offset the position within the input array at which to
 * start
 * @param len the number of bytes of input to encode
 * @param flags controls certain features of the encoded output.
 * Passing {@code DEFAULT} results in output that
 * adheres to RFC 2045.
 */
 public static byte[] encode(byte[] input, int offset, int len, int flags) {
 Encoder encoder = new Encoder(flags, null);
 
int output_len = len / 3 * 4;
 
if (encoder.do_padding) {
 if (len % 3 > 0) {
 output_len += 4;
 }
 } else {
 switch (len % 3) {
 case 0: break;
 case 1: output_len += 2; break;
 case 2: output_len += 3; break;
 }
 }
 
if (encoder.do_newline && len > 0) {
 output_len += (((len-1) / (3 * Encoder.LINE_GROUPS)) + 1) *
 (encoder.do_cr ? 2 : 1);
 }
 
encoder.output = new byte[output_len];
 encoder.process(input, offset, len, true);
 
assert encoder.op == output_len;
 
return encoder.output;
 }
 
static class Encoder extends Coder {
 /**
 * Emit a new line every this many output tuples. Corresponds to
 * a 76-character line length (the maximum allowable according to
 * <a href="http://www.ietf.org/rfc/rfc2045.txt">RFC 2045</a>).
 */
 public static final int LINE_GROUPS = 19;
 
/**
 * Lookup table for turning Base64 alphabet positions (6 bits)
 * into output bytes.
 */
 private static final byte ENCODE[] = {
 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/',
 };
 
/**
 * Lookup table for turning Base64 alphabet positions (6 bits)
 * into output bytes.
 */
 private static final byte ENCODE_WEBSAFE[] = {
 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', '_',
 };
 
final private byte[] tail;
 int tailLen;
 private int count;
 
final public boolean do_padding;
 final public boolean do_newline;
 final public boolean do_cr;
 final private byte[] alphabet;
 
public Encoder(int flags, byte[] output) {
 this.output = output;
 
do_padding = (flags & NO_PADDING) == 0;
 do_newline = (flags & NO_WRAP) == 0;
 do_cr = (flags & CRLF) != 0;
 alphabet = ((flags & URL_SAFE) == 0) ? ENCODE : ENCODE_WEBSAFE;
 
tail = new byte[2];
 tailLen = 0;
 
count = do_newline ? LINE_GROUPS : -1;
 }
 
/**
 * @return an overestimate for the number of bytes {@code
 * len} bytes could encode to.
 */
 @Override
public int maxOutputSize(int len) {
 return len * 8/5 + 10;
 }
 
@Override
public boolean process(byte[] input, int offset, int len, boolean finish) {
 final byte[] alphabet = this.alphabet;
 final byte[] output = this.output;
 int op = 0;
 int count = this.count;
 
int p = offset;
 len += offset;
 int v = -1;
 switch (tailLen) {
 case 0:
 break;
 
case 1:
 if (p+2 <= len) {
 v = ((tail[0] & 0xff) << 16) |
 ((input[p++] & 0xff) << 8) |
 (input[p++] & 0xff);
 tailLen = 0;
 };
 break;
 
case 2:
 if (p+1 <= len) {
 v = ((tail[0] & 0xff) << 16) |
 ((tail[1] & 0xff) << 8) |
 (input[p++] & 0xff);
 tailLen = 0;
 }
 break;
 }
 
if (v != -1) {
 output[op++] = alphabet[(v >> 18) & 0x3f];
 output[op++] = alphabet[(v >> 12) & 0x3f];
 output[op++] = alphabet[(v >> 6) & 0x3f];
 output[op++] = alphabet[v & 0x3f];
 if (--count == 0) {
 if (do_cr) output[op++] = '\r';
 output[op++] = '\n';
 count = LINE_GROUPS;
 }
 }
 
while (p+3 <= len) {
 v = ((input[p] & 0xff) << 16) |
 ((input[p+1] & 0xff) << 8) |
 (input[p+2] & 0xff);
 output[op] = alphabet[(v >> 18) & 0x3f];
 output[op+1] = alphabet[(v >> 12) & 0x3f];
 output[op+2] = alphabet[(v >> 6) & 0x3f];
 output[op+3] = alphabet[v & 0x3f];
 p += 3;
 op += 4;
 if (--count == 0) {
 if (do_cr) output[op++] = '\r';
 output[op++] = '\n';
 count = LINE_GROUPS;
 }
 }
 
if (finish) {
 
if (p-tailLen == len-1) {
 int t = 0;
 v = ((tailLen > 0 ? tail[t++] : input[p++]) & 0xff) << 4;
 tailLen -= t;
 output[op++] = alphabet[(v >> 6) & 0x3f];
 output[op++] = alphabet[v & 0x3f];
 if (do_padding) {
 output[op++] = '=';
 output[op++] = '=';
 }
 if (do_newline) {
 if (do_cr) output[op++] = '\r';
 output[op++] = '\n';
 }
 } else if (p-tailLen == len-2) {
 int t = 0;
 v = (((tailLen > 1 ? tail[t++] : input[p++]) & 0xff) << 10) |
 (((tailLen > 0 ? tail[t++] : input[p++]) & 0xff) << 2);
 tailLen -= t;
 output[op++] = alphabet[(v >> 12) & 0x3f];
 output[op++] = alphabet[(v >> 6) & 0x3f];
 output[op++] = alphabet[v & 0x3f];
 if (do_padding) {
 output[op++] = '=';
 }
 if (do_newline) {
 if (do_cr) output[op++] = '\r';
 output[op++] = '\n';
 }
 } else if (do_newline && op > 0 && count != LINE_GROUPS) {
 if (do_cr) output[op++] = '\r';
 output[op++] = '\n';
 }
 
assert tailLen == 0;
 assert p == len;
 } else {
 if (p == len-1) {
 tail[tailLen++] = input[p];
 } else if (p == len-2) {
 tail[tailLen++] = input[p];
 tail[tailLen++] = input[p+1];
 }
 }
 
this.op = op;
 this.count = count;
 
return true;
 }
 }
 
private B6U() { }
}