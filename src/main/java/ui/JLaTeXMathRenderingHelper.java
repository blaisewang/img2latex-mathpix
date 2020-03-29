package ui;

/*
 * Copyright 2013 Heaton Research, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * For more information on Heaton Research copyrights, licenses
 * and trademarks visit:
 * http://www.heatonresearch.com/copyright
 */

/*
  Modified by Blaise Wang on 29 March 2020
 */

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.scilab.forge.jlatexmath.ParseException;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;

import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Insets;
import java.awt.image.BufferedImage;


/**
 * UI.JLaTeXMathRenderingHelper.java
 * Utility class used to render LaTeX formulas.
 */
public class JLaTeXMathRenderingHelper {

    /**
     * Original source: https://github.com/jeffheaton/jlatexmath-example/blob/master/src/com/jeffheaton/latex/LatexExample.java
     *
     * @param latexSource text to be rendered.
     * @return rendered formula image.
     */
    public static Image render(String latexSource) {

        TeXFormula teXFormula;
        try {
            // create a formula
            teXFormula = new TeXFormula(latexSource);
        } catch (ParseException ignored) {
            return null;
        }

        // render the formula to an icon of the same size as the formula.
        var icon = teXFormula.createTeXIcon(TeXConstants.STYLE_DISPLAY, 20);

        // insert a border
        icon.setInsets(new Insets(5, 5, 5, 5));

        // create an image of the rendered formula
        var bufferedImage = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        var graphics2D = bufferedImage.createGraphics();
        graphics2D.setColor(Color.white);
        graphics2D.fillRect(0, 0, icon.getIconWidth(), icon.getIconHeight());
        var jLabel = new JLabel();
        jLabel.setForeground(new Color(0, 0, 0));
        icon.paintIcon(jLabel, graphics2D, 0, 0);

        return SwingFXUtils.toFXImage(bufferedImage, null);
    }

}
