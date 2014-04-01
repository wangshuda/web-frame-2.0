package com.cintel.frame.auth.random.image;

import java.io.IOException;
import java.io.OutputStream;

public interface IRandomImageGenerator {
	public String generateRandomStr();
	public void render(String randomStr, OutputStream out) throws IOException;
}
