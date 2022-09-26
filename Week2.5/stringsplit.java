public class stringsplit {
    public static void main(String[] args){
        String s = "abcdefg";
        String sub1 = s.substring(0, 6);
        String sub2 = s.substring(6,7);
        System.out.println(sub1);
        System.out.println(sub2);
        String combine = sub1+sub2;
        System.out.println(combine);
        System.out.println(s.charAt(6));
    }

}
