package com.capgemini.mf2hadoop.utils;

import com.capgemini.mf2hadoop.constants.Mf2HadoopConstants;


public class MF2HadoopUtils {
	public static final int UNSIGNED_BYTE = 0xff;
	public static final int BITS_RIGHT = 0xf;
	  
	
	public static int extractAtomicFieldLength(String dataType){
		int fieldLengthInBytes =0;
		if(dataType.startsWith("X")){
			fieldLengthInBytes = getStringFieldLength(dataType);
		}else if(dataType.startsWith("S")){
			fieldLengthInBytes = getSignedNumberFieldLength(dataType);
		}else{
			fieldLengthInBytes = getUnsignedNumberFieldLength(dataType);
		}
		return fieldLengthInBytes;
	}
	
	public static String fieldType(String dataType){
		String type;
		if(dataType.startsWith("X")){
			type = Mf2HadoopConstants.STRING_FIELD;
		}else if(dataType.startsWith("S")){
			type = Mf2HadoopConstants.SIGNED_NUMBER;
		}else{
			type = Mf2HadoopConstants.UNSIGNED_NUMBER;
		}
		return type;
	}
	
	public static int isComputational(String dataType){
		return dataType.indexOf("COMP");
	}
	
	public static String unpackData(byte[] packedData, int decimalPointLocation) {
        String unpackedData = "";
        final int negativeSign = 13;
        for (int currentByteIndex = 0; currentByteIndex < packedData.length; currentByteIndex++) {

            int firstDigit = ((packedData[currentByteIndex] >> 4) & 0x0f);
            int secondDigit = (packedData[currentByteIndex] & 0x0F);
            unpackedData += String.valueOf(firstDigit);
            if (currentByteIndex == (packedData.length - 1)) {
                if (secondDigit == negativeSign) {
                    unpackedData = "-" + unpackedData;
                }
            } else {
                unpackedData += String.valueOf(secondDigit);
            }
        }
        if (decimalPointLocation > 0) {
            unpackedData = unpackedData.substring(0, (decimalPointLocation - 1)) +
                            "." +
                            unpackedData.substring(decimalPointLocation);
        }
        return unpackedData;
    }
		
	private static int getSignedNumberFieldLength(String dataType) {
		return (getUnsignedNumberFieldLength(dataType.substring(1)));
	}

	private static int getStringFieldLength(String dataType) {
		int len =0;
		len = getParameterizedAndNonParameterizedLength(dataType);
		return len;
	}

	private static int getUnsignedNumberFieldLength(String dataType) {
		int len =0;
		int computational= isComputational(dataType);
		String compution_type = dataType;
		dataType= computational>=0?dataType.substring(0, computational).trim():dataType;
		if(dataType.contains("V")){
			String[] split = dataType.split("V");
			int len1= getParameterizedAndNonParameterizedLength(split[0]);
			int len2= getParameterizedAndNonParameterizedLength(split[1]);
			len+= len1+len2;
		}else{
			len+= getParameterizedAndNonParameterizedLength(dataType);
		}
		
		if(computational>=0){
			compution_type = compution_type.substring(computational);
			if(compution_type.startsWith("COMP-")){
				String type = compution_type.substring(5);
				if(type.equalsIgnoreCase("3")){
					double doubleLength = (double)(len+1)/2;
					double ceil = Math.ceil(doubleLength);
					len=(int)ceil;
				}else if(type.equalsIgnoreCase("2")){
					len = 8;
				}else if(type.equalsIgnoreCase("1")){
					len = 4;
				}
			}
				else{
				if(len <= 4 && len >= 0){
					len = 2;
				}if(len <=9 && len >= 5){
					len = 4;
				}if(len >= 18 && len >=10){
					len = 8;
				}	
			}
		}
		return len;
	}
	
	private static int getParameterizedAndNonParameterizedLength(String dataType){
		int len=0;
		if(dataType.indexOf(Mf2HadoopConstants.OPENING_PARENTHESIS)!=-1){
			int openParan = dataType.indexOf(Mf2HadoopConstants.OPENING_PARENTHESIS);
			int closeParan = dataType.indexOf(Mf2HadoopConstants.CLOSING_PARENTHESIS);
			if(closeParan-openParan==2){
				len = Integer.parseInt(dataType.substring(openParan +1, openParan +2));
			}else{
				len = Integer.parseInt(dataType.substring(dataType.indexOf(Mf2HadoopConstants.OPENING_PARENTHESIS) + 1, dataType.indexOf(Mf2HadoopConstants.CLOSING_PARENTHESIS)));
			}
		}else{
			len = dataType.trim().length();
		}
		return len;
	}

	public static int getDecimalPosition(String dataType) {
		int pos=0;
		if(dataType.contains("V")){
			String[] split = dataType.split("V");
			int len1= getParameterizedAndNonParameterizedLength(split[0]);
			pos=len1+1;
		}
		return pos;
	}
    


}
