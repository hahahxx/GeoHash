public class GeoHash {
  /**
   * @convert Binary String to Integer
   * @return a Integer
   */
   private int toInt (String str) {
       return Integer.parseInt(str, 2);
   }

   /**
    * @convert String to Double
    */
   private double strToDou (String str, double min, double max) {
       for (int i = 0; i < str.length(); i++) {
           double mid = (min + max)/2;
           if (str.charAt(i) == '1') {
               min = mid;
           }
           else {
               max = mid;
           }
       }
       return (min + max)/2;
   }

   /**
    * @return a base32 string
    */
    public String encode(double latitude, double longitude, int precision) {
        // Write your code here
        int len = precision * 5;
        int lenLat = len / 2;
        int lenLgt = len - lenLat;
        StringBuffer lat = new StringBuffer();
        StringBuffer lgt = new StringBuffer();
        double[] range = {-90.0, 90.0};
        for (int i = 0; i < lenLat; i++) {
            double mid = (range[0] + range[1])/2;
            if (latitude <= mid) {
                lat.append('0');
                range[1] = mid;
            }
            else {
                lat.append('1');
                range[0] = mid;
            }
        }
        range[0] = -180.0;
        range[1] = 180.0;
        for (int i = 0; i < lenLgt; i++) {
            double mid = (range[0] + range[1])/2;
            if (longitude <= mid) {
                lgt.append('0');
                range[1] = mid;
            }
            else {
                lgt.append('1');
                range[0] = mid;
            }
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < lenLgt; i++) {
            sb.append(lgt.charAt(i));
            if (i < lenLat) {
                sb.append(lat.charAt(i));
            }
        }
        String base32 = "0123456789bcdefghjkmnpqrstuvwxyz";
        StringBuffer ret = new StringBuffer();
        for (int i = 0; i < len; i = i + 5) {
            String str = sb.substring(i, i + 5);
            int index = toInt(str);
            ret.append(base32.charAt(index));
        }
        return ret.toString();
    }

    /**
     * @return lat and lgt in double[] ary
     */
    public double[] decode(String geohash) {
        // Write your code here
        double[] ret = new double[2];
        if (geohash.length() == 0)
            return ret;
        StringBuffer geo = new StringBuffer();
        for (int i = 0; i < geohash.length(); i++) {
            String _base32 = "0123456789bcdefghjkmnpqrstuvwxyz";
            int tmp = _base32.indexOf(geohash.charAt(i));
            String tmpStr = Integer.toBinaryString(tmp);
            int count = 5 - tmpStr.length();
            for (int j = 0; j < count; j++) {
                geo.append('0');
            }
            geo.append(tmpStr);
        }
        StringBuffer latSB = new StringBuffer();
        StringBuffer lngSB = new StringBuffer();
        for (int i = 0; i < geo.length(); i++) {
            if (i % 2 == 0) {
                lngSB.append(geo.charAt(i));
            }
            else {
                latSB.append(geo.charAt(i));
            }
        }
        ret[0] = strToDou(latSB.toString(), -90, 90);
        ret[1] = strToDou(lngSB.toString(), -180, 180);
        return ret;
    }
}
