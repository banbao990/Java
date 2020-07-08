/**
 * @author banbao
 */

class Print{
    // package access
    static void p(Object s){
        System.out.println(s);
    }
}

public class ManyNestedClass{
    private class O1{
        // ͬ���ֶ�
        private int o = 1;
        // ͬ������
        private void f(){ Print.p("O1.f"); }
        private void f1(){ Print.p("f1"); }
        class O2{
            // ͬ���ֶ�
            int o = 2;
            // ͬ������
            void f(){ Print.p("O2.f"); }
            void f2(){ Print.p("f2"); }
            public class O3{
                // ͬ���ֶ�
                public int o = 3;
                // ͬ������
                void f(){ Print.p("O3.f"); }
                public void f3(){
                    // �����ⲿ�ֶ�(���⵽��)
                    Print.p("-----");
                    Print.p(O1.this.o);
                    Print.p(O2.this.o);
                    Print.p(this.o);
                    Print.p("-----");
                    // �����ⲿ����
                    f1();
                    f2();
                    Print.p("-----");
                    // ͬ������(���⵽��)
                    O1.this.f();
                    O2.this.f();
                    this.f();
                }
            }
        }
    }
    public static void main(String...args){
        ManyNestedClass t0 = new ManyNestedClass();
        ManyNestedClass.O1 t1 = t0.new O1();
        ManyNestedClass.O1.O2 t2 = t1.new O2();
        ManyNestedClass.O1.O2.O3 t3 = t2.new O3();
        t3.f3();
    }
}

/*
 * output
 * 
 * -----
 * 1
 * 2
 * 3
 * -----
 * f1
 * f2
 * -----
 * O1.f
 * O2.f
 * O3.f
*/