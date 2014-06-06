package org.tsaikd.java.weblire.servlet;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tsaikd.java.weblire.ext.CEDDext;
import org.tsaikd.java.weblire.ext.ColorLayoutext;
import org.tsaikd.java.weblire.util.HashUtils;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;

@WebServlet(urlPatterns = { "/features" })
@MultipartConfig
public class WebApi_Features extends HttpServlet {

	private static final long serialVersionUID = 1L;
	static final Log log = LogFactory.getLog(WebApi_Features.class);

	private boolean hasFlag(HttpServletRequest req, String name) {
		String value = req.getParameter(name);
		if (value == null) {
			return false;
		}
		if (value.equalsIgnoreCase("false")) {
			return false;
		}
		if (value.equalsIgnoreCase("off")) {
			return false;
		}
		return true;
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		hasFlag(req, "image");
		hasFlag(req, "sha512");
		hasFlag(req, "CEED");
		Part filePart = req.getPart("image");
		InputStream is = filePart.getInputStream();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		org.apache.commons.io.IOUtils.copy(is, baos);
		is.close();

		byte[] data = baos.toByteArray();
		BufferedImage image = ImageIO.read(new ByteArrayInputStream(data));
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		ImageInputStream iis = ImageIO.createImageInputStream(bais);
		Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
		ImageReader reader = readers.next();
		iis.close();

		DBObject picinfo = BasicDBObjectBuilder.start()
				.add("fileSize", filePart.getSize())
				.add("width", image.getWidth())
				.add("height", image.getHeight())
				.add("fileType", reader.getFormatName()).get();

		if (hasFlag(req, "sha512")) {
			picinfo.put("sha512", HashUtils.sha512(data));
		}

		if (hasFlag(req, "CEED")) {
			CEDDext ceedext = new CEDDext();
			ceedext.extract(image);
			double sumCEED = ceedext.getSum();
			picinfo.put("featureCEDD", Hex.encodeHexString(ceedext.getByteArrayRepresentation()));
			picinfo.put("sumCEED", sumCEED);
			picinfo.put("stdDiffCEED", ceedext.getStdDiffPow2(sumCEED));
		}

		if (hasFlag(req, "ColorLayoutext")) {
			ColorLayoutext colorLayoutext = new ColorLayoutext();
			colorLayoutext.extract(image);
			picinfo.put("descriptorColorLayout", Hex.encodeHexString(colorLayoutext.getByteArrayRepresentation()));
			double sumColorLayoutY = colorLayoutext.getSumY();
			picinfo.put("sumColorLayoutY", sumColorLayoutY);
			picinfo.put("stdDiffColorLayoutY", colorLayoutext.getStdDiffPow2Y(sumColorLayoutY));
			double sumColorLayoutC = colorLayoutext.getSumC();
			picinfo.put("sumColorLayoutC", sumColorLayoutC);
			picinfo.put("stdDiffColorLayoutC", colorLayoutext.getStdDiffPow2C(sumColorLayoutC));
		}

		PrintWriter writer = res.getWriter();
		writer.write(picinfo.toString());
		writer.close();
	}

}
