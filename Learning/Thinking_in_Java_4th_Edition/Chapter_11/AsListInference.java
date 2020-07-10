// ʾ������

import java.util.*;

class Snow {}
class Powder extends Snow {}
class Light extends Powder {}
class Heavy extends Powder {}
class Crusty extends Snow {}
class Slush extends Snow {}

public class AsListInference {
    public static void main(String[] args) {
        // case 1: OK
        List<Snow> snow1 = Arrays.asList(
            new Crusty(), new Slush(), new Powder());

        // case 2: OK
        // �������ܹ�ʶ�����(1.8)
        // ���� 1.6 ����̫��(û�в���)
        List<Snow> snow2 = Arrays.asList(
            new Light(), new Heavy());

        // case 3: OK
        // Collections �� OK
        List<Snow> snow3 = new ArrayList<Snow>();
        Collections.addAll(snow3, new Light(), new Heavy());

        // case 4: OK
        // ������ʾ��Ϣ
        List<Snow> snow4 = Arrays.<Snow>asList(
             new Light(), new Heavy());
    }
}