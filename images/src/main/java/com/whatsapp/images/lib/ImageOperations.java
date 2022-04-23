package com.whatsapp.images.lib;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public interface ImageOperations {
      BufferedImage read(InputStream is) throws IOException;

      void write(BufferedImage img, String format, File file) throws IOException;

      public static ImageOperations getCommonsIoInstance() {
            return new CommonsIoImageOperations();
      }

      public class CommonsIoImageOperations implements ImageOperations {

            @Override
            public BufferedImage read(InputStream is) throws IOException {
                  return ImageIO.read(is);
            }

            @Override
            public void write(BufferedImage img, String format, File file) throws IOException {
                  ImageIO.write(img, format, file);
            }
      }
}
