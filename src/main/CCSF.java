package main;

import com.binaryfilereader.BinaryReader;
//import filetypes.animation.Type20;
//import filetypes.animation.TypeFF01;
import filetypes.model.BMP;
import filetypes.model.CLT;
import filetypes.model.MDL;
import utils.FileUtils;

//import filetypes.model.Type1;
//import filetypes.model.TypeC;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;
import javax.imageio.ImageIO;

public class CCSF {
	private ArrayList<String> unknownTypes = new ArrayList();

	private BinaryReader br;

	private String fileName;

	private String[] fileNames;
	private String[] objectNames;
	private String path;
	private boolean ignoreMDL;
//z	private ArrayList<TypeC> typeC = new ArrayList();
//	private ArrayList<filetypes.model.Type9> type9 = new ArrayList();
	public ArrayList<MDL> meshes = new ArrayList();
//	private ArrayList<filetypes.model.Type2> type2 = new ArrayList();
//	private ArrayList<Type1> type1 = new ArrayList();
	private ArrayList<BMP> images = new ArrayList();
	private ArrayList<CLT> clt = new ArrayList();

//	private ArrayList<filetypes.animation.TypeA> typeA = new ArrayList();
//	private ArrayList<Type20> type20 = new ArrayList();
//	private ArrayList<filetypes.animation.Type7> type7 = new ArrayList();
//	public ArrayList<filetypes.animation.TypeB> typeB = new ArrayList();
//	private ArrayList<TypeFF01> typeFF01 = new ArrayList();

	public CCSF(File ccsFile, boolean ignoreMDL) {
		this.ignoreMDL = ignoreMDL;
		fileName = ccsFile.getName();
		try {
			if ((ccsFile.getAbsolutePath().endsWith(".tmp")) || (ccsFile.getAbsolutePath().endsWith(".cmp"))) {
				InputStream is = new FileInputStream(ccsFile.getAbsolutePath());
				br = new BinaryReader(is);
			} else if (ccsFile.getAbsolutePath().endsWith(".CCS")) {
				if(FileUtils.isGZipped(ccsFile)) {
					FileInputStream fin = new FileInputStream(ccsFile.getAbsolutePath());
					GZIPInputStream gzis = new GZIPInputStream(fin);
					br = new BinaryReader(gzis);
				} else {
					FileInputStream fin = new FileInputStream(ccsFile.getAbsolutePath());
					br = new BinaryReader(fin);
				}
			} else {
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		path = (ccsFile.getParent() + "\\output\\");
		readContents();
		outputImages();
		exportMeshes();
	}

	private void readContents() {
		if (br.readInt() != -859045887) { // 0xCCCC0001		
			System.out.println("Not a CCS file?");
			return;
		}
		br.skip(4);
		br.readString(36);
		br.skip(24);
		fileNames = new String[br.readInt() - 1];
		objectNames = new String[br.readInt() - 1];
		br.skip(32);
		for (int i = 0; i < fileNames.length; i++) {
			fileNames[i] = br.readString(32);
		}
		br.skip(32);
		for (int i = 0; i < objectNames.length; i++) {
			objectNames[i] = br.readString(32);
		}
		br.skip(8);
		int fileType = 0;

		while ((br.getSize() - br.getOffset() >= 4) && ((fileType = br.readInt()) != -859045883)) { // 0xCCCC0005

			switch (fileType) {

			case -859043840: // 0xCCCC0800
				byte[] data = br.readBytes(br.readInt() * 4);
				if (!ignoreMDL) {
					MDL m = null;
					try {
						m = new MDL(data);
					} catch (Exception localException) {
					}

					if ((m != null) && m.successful)
						meshes.add(m);
				}
				break;
			case -859044864: // 0xCCCC0400
				data = br.readBytes(br.readInt() * 4);
				clt.add(new CLT(data));
				break;
			case -859045120: // 0xCCCC0300
				data = br.readBytes(br.readInt() * 4 - 200);
				images.add(new BMP(data, clt));
				clt = new ArrayList();
				break;
			default:
				br.skip(br.readInt() * 4);
			}

		}
	}

	private void outputImages() {
		if (!new File(path).exists()) {
			new File(path).mkdir();
		}
		ArrayList<BufferedImage> images = new ArrayList();
		for (int i = 0; i < this.images.size(); i++) {
			for (int j = 0; j < ((BMP) this.images.get(i)).getImages().length; j++) {
				images.add(((BMP) this.images.get(i)).getImages()[j]);
			}
		}
		for (int i = 0; i < images.size(); i++) {

			File f = new File(path + fileName + "." + i + ".png");
			try {
				ImageIO.write((RenderedImage) images.get(i), "png", f);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void exportMeshes() {
		for (int i = 0; i < meshes.size(); i++) {
			try {
				if (!new File(path).exists()) {
					new File(path).mkdir();
				}
				new FileWriter(path + fileName + "." + i + ".obj").append(((MDL) meshes.get(i)).exportOBJ()).close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
