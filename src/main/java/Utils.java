import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.IOException;

class Util {

    private static BufferedImage toBufferedImage(Image image) {

        if (image instanceof BufferedImage) {

            return (BufferedImage) image;

        }

        BufferedImage buffered = new BufferedImage(image.getWidth(null), image.getHeight(null),
                BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics2D = buffered.createGraphics();
        graphics2D.drawImage(image, 0, 0, null);
        graphics2D.dispose();

        return buffered;

    }

    static BufferedImage getImageFromClipboard() throws IOException, UnsupportedFlavorException {

        Transferable transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);

        if (transferable.isDataFlavorSupported(DataFlavor.imageFlavor)) {

            return toBufferedImage((Image) transferable.getTransferData(DataFlavor.imageFlavor));

        } else {

            return null;

        }

    }

    static String replaceDoubleDollarWithWrapper(String doubleDollarResult) {

        String result = doubleDollarResult.replaceFirst("\\$\\$\n", "\\\\begin{equation}\n");
        return result.replaceFirst("\n\\$\\$", "\n\\\\end{equation}");

    }

}
