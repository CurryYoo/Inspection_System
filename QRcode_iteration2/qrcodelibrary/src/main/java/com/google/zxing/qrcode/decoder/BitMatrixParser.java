//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.google.zxing.qrcode.decoder;

import com.google.zxing.FormatException;
import com.google.zxing.common.BitMatrix;

final class BitMatrixParser {
    private final BitMatrix bitMatrix;
    private Version parsedVersion;
    private FormatInformation parsedFormatInfo;

    BitMatrixParser(BitMatrix bitMatrix) throws FormatException {
        int dimension = bitMatrix.getHeight();
        if(dimension >= 21 && (dimension & 3) == 1) {
            this.bitMatrix = bitMatrix;
        } else {
            throw FormatException.getFormatInstance();
        }
    }

    FormatInformation readFormatInformation() throws FormatException {
        if(this.parsedFormatInfo != null) {
            return this.parsedFormatInfo;
        } else {
            int formatInfoBits1 = 0;

            int dimension;
            for(dimension = 0; dimension < 6; ++dimension) {
                formatInfoBits1 = this.copyBit(dimension, 8, formatInfoBits1);
            }

            formatInfoBits1 = this.copyBit(7, 8, formatInfoBits1);
            formatInfoBits1 = this.copyBit(8, 8, formatInfoBits1);
            formatInfoBits1 = this.copyBit(8, 7, formatInfoBits1);

            for(dimension = 5; dimension >= 0; --dimension) {
                formatInfoBits1 = this.copyBit(8, dimension, formatInfoBits1);
            }

            dimension = this.bitMatrix.getHeight();
            int formatInfoBits2 = 0;
            int iMin = dimension - 8;

            int j;
            for(j = dimension - 1; j >= iMin; --j) {
                formatInfoBits2 = this.copyBit(j, 8, formatInfoBits2);
            }

            for(j = dimension - 7; j < dimension; ++j) {
                formatInfoBits2 = this.copyBit(8, j, formatInfoBits2);
            }

            this.parsedFormatInfo = FormatInformation.decodeFormatInformation(formatInfoBits1, formatInfoBits2);
            if(this.parsedFormatInfo != null) {
                return this.parsedFormatInfo;
            } else {
                throw FormatException.getFormatInstance();
            }
        }
    }

    Version readVersion() throws FormatException {
        if(this.parsedVersion != null) {
            return this.parsedVersion;
        } else {
            int dimension = this.bitMatrix.getHeight();
            int provisionalVersion = dimension - 17 >> 2;
            if(provisionalVersion <= 6) {
                return Version.getVersionForNumber(provisionalVersion);
            } else {
                int versionBits = 0;
                int ijMin = dimension - 11;

                int i;
                int j;
                for(i = 5; i >= 0; --i) {
                    for(j = dimension - 9; j >= ijMin; --j) {
                        versionBits = this.copyBit(j, i, versionBits);
                    }
                }

                this.parsedVersion = Version.decodeVersionInformation(versionBits);
                if(this.parsedVersion != null && this.parsedVersion.getDimensionForVersion() == dimension) {
                    return this.parsedVersion;
                } else {
                    versionBits = 0;

                    for(i = 5; i >= 0; --i) {
                        for(j = dimension - 9; j >= ijMin; --j) {
                            versionBits = this.copyBit(i, j, versionBits);
                        }
                    }

                    this.parsedVersion = Version.decodeVersionInformation(versionBits);
                    if(this.parsedVersion != null && this.parsedVersion.getDimensionForVersion() == dimension) {
                        return this.parsedVersion;
                    } else {
                        throw FormatException.getFormatInstance();
                    }
                }
            }
        }
    }

    private int copyBit(int i, int j, int versionBits) {
        return this.bitMatrix.get(i, j)?versionBits << 1 | 1:versionBits << 1;
    }

    byte[] readCodewords() throws FormatException {
        FormatInformation formatInfo = this.readFormatInformation();
        Version version = this.readVersion();
        DataMask dataMask = DataMask.forReference(formatInfo.getDataMask());
        int dimension = this.bitMatrix.getHeight();
        dataMask.unmaskBitMatrix(this.bitMatrix, dimension);
        BitMatrix functionPattern = version.buildFunctionPattern();
        boolean readingUp = true;
        byte[] result = new byte[version.getTotalCodewords()];
        int resultOffset = 0;
        int currentByte = 0;
        int bitsRead = 0;

        for(int j = dimension - 1; j > 0; j -= 2) {
            if(j == 6) {
                --j;
            }

            for(int count = 0; count < dimension; ++count) {
                int i = readingUp?dimension - 1 - count:count;

                for(int col = 0; col < 2; ++col) {
                    if(!functionPattern.get(j - col, i)) {
                        ++bitsRead;
                        currentByte <<= 1;
                        if(this.bitMatrix.get(j - col, i)) {
                            currentByte |= 1;
                        }

                        if(bitsRead == 8) {
                            result[resultOffset++] = (byte)currentByte;
                            bitsRead = 0;
                            currentByte = 0;
                        }
                    }
                }
            }

            readingUp ^= true;
        }

        if(resultOffset != version.getTotalCodewords()) {
            throw FormatException.getFormatInstance();
        } else {
            return result;
        }
    }
}
