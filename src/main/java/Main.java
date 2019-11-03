import org.apache.commons.lang3.SystemUtils;

import java.awt.Toolkit;


public class Main {

    public static void main(String[] args) {

        if (SystemUtils.IS_OS_MAC_OSX) {

            System.setProperty("apple.awt.UIElement", "true");
            Toolkit.getDefaultToolkit();

        }

        MainAPP.main(args);

    }

}
