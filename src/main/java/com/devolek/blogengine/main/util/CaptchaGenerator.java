package com.devolek.blogengine.main.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Component
public class CaptchaGenerator {
    @Value("${captcha.width}")
    private int width;
    @Value("${captcha.height}")
    private int height;
    @Value("${captcha.font-size}")
    private int fontSize;

    public Map<String, String> getCaptcha() throws IOException {

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = image.createGraphics();
        graphics2D.setColor(Color.WHITE);
        graphics2D.fillRect(0, 0, width, height);

        Random r = new Random();
        String token = Long.toString(Math.abs(r.nextLong()), 36);
        String ch = token.substring(0, 6);

        Color c = new Color(0.6662f, 0.4569f, 0.3232f);
        GradientPaint gp = new GradientPaint(30, 30, c, 15, 25, Color.DARK_GRAY, true);
        graphics2D.setPaint(gp);
        Font font = new Font("DroidSans", Font.BOLD, fontSize);
        graphics2D.setFont(font);
        FontMetrics fm = graphics2D.getFontMetrics();
        int x = (width - fm.stringWidth(ch)) / 2;
        int y = (fm.getAscent() + (height - (fm.getAscent() + fm.getDescent())) / 2);
        graphics2D.drawString(ch, x, y);
        graphics2D.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baos);
        baos.flush();
        byte[] fileContent = baos.toByteArray();
        baos.close();

        String encodedString = Base64.getEncoder().encodeToString(fileContent);

        Map<String, String> map = new HashMap<>();
        map.put("secret", ch);
        map.put("image", encodedString);
        return map;
    }
}
