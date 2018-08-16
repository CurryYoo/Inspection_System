//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.google.zxing.common;

public final class BitArray {
    public int[] bits;
    public int size;

    public BitArray() {
        this.size = 0;
        this.bits = new int[1];
    }

    public BitArray(int size) {
        this.size = size;
        this.bits = makeArray(size);
    }

    public int getSize() {
        return this.size;
    }

    public int getSizeInBytes() {
        return this.size + 7 >> 3;
    }

    private void ensureCapacity(int size) {
        if(size > this.bits.length << 5) {
            int[] newBits = makeArray(size);
            System.arraycopy(this.bits, 0, newBits, 0, this.bits.length);
            this.bits = newBits;
        }

    }

    public boolean get(int i) {
        return (this.bits[i >> 5] & 1 << (i & 31)) != 0;
    }

    public void set(int i) {
        this.bits[i >> 5] |= 1 << (i & 31);
    }

    public void flip(int i) {
        this.bits[i >> 5] ^= 1 << (i & 31);
    }

    public void setBulk(int i, int newBits) {
        this.bits[i >> 5] = newBits;
    }

    public void clear() {
        int max = this.bits.length;

        for(int i = 0; i < max; ++i) {
            this.bits[i] = 0;
        }

    }

    public boolean isRange(int start, int end, boolean value) {
        if(end < start) {
            throw new IllegalArgumentException();
        } else if(end == start) {
            return true;
        } else {
            --end;
            int firstInt = start >> 5;
            int lastInt = end >> 5;

            for(int i = firstInt; i <= lastInt; ++i) {
                int firstBit = i > firstInt?0:start & 31;
                int lastBit = i < lastInt?31:end & 31;
                int mask;
                if(firstBit == 0 && lastBit == 31) {
                    mask = -1;
                } else {
                    mask = 0;

                    for(int j = firstBit; j <= lastBit; ++j) {
                        mask |= 1 << j;
                    }
                }

                if((this.bits[i] & mask) != (value?mask:0)) {
                    return false;
                }
            }

            return true;
        }
    }

    public void appendBit(boolean bit) {
        this.ensureCapacity(this.size + 1);
        if(bit) {
            this.bits[this.size >> 5] |= 1 << (this.size & 31);
        }

        ++this.size;
    }

    public void appendBits(int value, int numBits) {
        if(numBits >= 0 && numBits <= 32) {
            this.ensureCapacity(this.size + numBits);

            for(int numBitsLeft = numBits; numBitsLeft > 0; --numBitsLeft) {
                this.appendBit((value >> numBitsLeft - 1 & 1) == 1);
            }

        } else {
            throw new IllegalArgumentException("Num bits must be between 0 and 32");
        }
    }

    public void appendBitArray(BitArray other) {
        int otherSize = other.getSize();
        this.ensureCapacity(this.size + otherSize);

        for(int i = 0; i < otherSize; ++i) {
            this.appendBit(other.get(i));
        }

    }

    public void xor(BitArray other) {
        if(this.bits.length != other.bits.length) {
            throw new IllegalArgumentException("Sizes don\'t match");
        } else {
            for(int i = 0; i < this.bits.length; ++i) {
                this.bits[i] ^= other.bits[i];
            }

        }
    }

    public void toBytes(int bitOffset, byte[] array, int offset, int numBytes) {
        for(int i = 0; i < numBytes; ++i) {
            int theByte = 0;

            for(int j = 0; j < 8; ++j) {
                if(this.get(bitOffset)) {
                    theByte |= 1 << 7 - j;
                }

                ++bitOffset;
            }

            array[offset + i] = (byte)theByte;
        }

    }

    public int[] getBitArray() {
        return this.bits;
    }

    public void reverse() {
        int[] newBits = new int[this.bits.length];
        int size = this.size;

        for(int i = 0; i < size; ++i) {
            if(this.get(size - i - 1)) {
                newBits[i >> 5] |= 1 << (i & 31);
            }
        }

        this.bits = newBits;
    }

    private static int[] makeArray(int size) {
        return new int[size + 31 >> 5];
    }

    public String toString() {
        StringBuffer result = new StringBuffer(this.size);

        for(int i = 0; i < this.size; ++i) {
            if((i & 7) == 0) {
                result.append(' ');
            }

            result.append((char)(this.get(i)?'X':'.'));
        }

        return result.toString();
    }
}
