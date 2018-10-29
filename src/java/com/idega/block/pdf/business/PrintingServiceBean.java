/*
 * $Id: PrintingServiceBean.java,v 1.13 2008/10/23 12:28:06 valdas Exp $ Created
 * on 15.10.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf. Use is subject to
 * license terms.
 */
package com.idega.block.pdf.business;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;

import org.apache.commons.io.IOUtils;
import org.ujac.print.DocumentHandlerException;
import org.ujac.print.DocumentPrinter;
import org.ujac.util.io.FileResourceLoader;
import org.ujac.util.io.HttpResourceLoader;

import com.idega.block.pdf.data.DocumentURIEntity;
import com.idega.block.pdf.presentation.handler.Base64ImageTagProcessor;
import com.idega.business.IBORuntimeException;
import com.idega.business.IBOServiceBean;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplication;
import com.idega.idegaweb.IWMainApplicationSettings;
import com.idega.servlet.filter.IWBundleResourceFilter;
import com.idega.util.StringHandler;
import com.idega.util.StringUtil;
import com.idega.util.datastructures.map.MapUtil;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.Pipeline;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.css.CssFilesImpl;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.HTML;
import com.itextpdf.tool.xml.html.TagProcessorFactory;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;


/**
 *
 * Last modified: $Date: 2008/10/23 12:28:06 $ by $Author: valdas $
 *
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.13 $
 */
public class PrintingServiceBean extends IBOServiceBean implements PrintingService {

	private static final long serialVersionUID = 5645957534865246451L;

	/*
	 * // defining the document properties, this map is used for dynamical content
	 * evaluation. Map documentProperties = new HashMap(); ... // instantiating
	 * the document printer FileInputStream templateStream = new
	 * FileInputStream("your-template-file.xml"); DocumentPrinter documentPrinter =
	 * new DocumentPrinter(templateStream, documentProperties); // in case you'd
	 * like to use a XML parser different from the default crimson implementation //
	 * you can specify it here (apache xerces in this case).
	 * documentPrinter.setXmlReaderClass("org.apache.xerces.parsers.SAXParser"); //
	 * defining the ResourceLoader: This is necessary if you like to //
	 * dynamically load resources like images during template processing.
	 * documentPrinter.setResourceLoader(new FileResourceLoader("./")); //
	 * generating the document output FileOutputStream pdfStream = new
	 * FileOutputStream("your-output-file.pdf");
	 * documentPrinter.printDocument(pdfStream);
	 */

	/*
	 * (non-Javadoc)
	 * @see com.idega.block.pdf.business.PrintingService#print(java.io.InputStream, java.lang.String)
	 */
	@Override
	public void printIText(
			InputStream inputStream,
			OutputStream outputStream,
			String documentResourcesFolder,
			Map<String, String> expressions
			) throws DocumentHandlerException, IOException {
		if (inputStream != null && outputStream != null) {
			if (StringUtil.isEmpty(documentResourcesFolder)) {
				documentResourcesFolder = getIWApplicationContext().getDomain().getURL() + "content/files/public/";
			}

			DocumentPrinter documentPrinter = new DocumentPrinter();
			if (!MapUtil.isEmpty(expressions)) {
				documentPrinter.setProperties(expressions);
			}

			documentPrinter.setTemplateSource(inputStream);
			documentPrinter.setResourceLoader(new HttpResourceLoader(documentResourcesFolder));
			documentPrinter.printDocument(outputStream);
		}
	}

	@Override
	public ByteArrayOutputStream printIText(
			InputStream inputStream,
			String documentResourcesFolder,
			Map<String, String> expressions
			) throws DocumentHandlerException, IOException {
		ByteArrayOutputStream outputStream = null;
		if (inputStream != null) {
			try {
				outputStream = new ByteArrayOutputStream(inputStream.available());
			} catch (IOException e) {
				outputStream = new ByteArrayOutputStream();
			}

			printIText(inputStream, outputStream, documentResourcesFolder, expressions);
		}

		return outputStream;
	}

	/*
	 * (non-Javadoc)
	 * @see com.idega.block.pdf.business.PrintingService#printXHTML(java.io.InputStream, java.io.OutputStream)
	 */
	@Override
	public void printXHTML(InputStream inputStream, OutputStream outputStream) {
		if (inputStream != null && outputStream != null) {
			IWMainApplicationSettings settings = IWMainApplication
					.getDefaultIWMainApplication().getSettings();
			if (!settings.getBoolean("iText_fonts_registered", Boolean.FALSE)) {
				FontFactory.registerDirectories();
				settings.setProperty("iText_fonts_registered", Boolean.TRUE.toString());
			}

			Document document = new Document();
		    PdfWriter writer = null;
			try {
				writer = PdfWriter.getInstance(document, outputStream);
			} catch (DocumentException e) {
				getLogger().log(Level.WARNING,
						"Failed to initialize " + PdfWriter.class.getSimpleName() +
						" cause of: ", e);
			}

			document.open();

			final TagProcessorFactory tagProcessorFactory = Tags.getHtmlTagProcessorFactory();
	        tagProcessorFactory.removeProcessor(HTML.Tag.IMG);
	        tagProcessorFactory.addProcessor(new Base64ImageTagProcessor(), HTML.Tag.IMG);

	        final HtmlPipelineContext hpc = new HtmlPipelineContext(new CssAppliersImpl(new XMLWorkerFontProvider()));
	        hpc.setAcceptUnknown(true).autoBookmark(true).setTagFactory(tagProcessorFactory);
	        final HtmlPipeline htmlPipeline = new HtmlPipeline(hpc, new PdfWriterPipeline(document, writer));

	        final CssFilesImpl cssFiles = new CssFilesImpl();
	        cssFiles.add(XMLWorkerHelper.getInstance().getDefaultCSS());
	        final StyleAttrCSSResolver cssResolver = new StyleAttrCSSResolver(cssFiles);
	        final Pipeline<?> pipeline = new CssResolverPipeline(cssResolver, htmlPipeline);
	        final XMLWorker worker = new XMLWorker(pipeline, true);
	        final XMLParser xmlParser = new XMLParser(true, worker);

		    try {
				xmlParser.parse(inputStream);
			} catch (IOException e) {
				getLogger().log(Level.WARNING,
						"Failed to parse XHTML to PDF cause of:", e);
			}

		    try {
			    document.close();
		    } catch (Exception e) {
		    	getLogger().log(Level.WARNING,
						"Failed toclose document cause of:", e);
		    }
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.idega.block.pdf.business.PrintingService#printXHTML(java.io.InputStream, java.io.OutputStream)
	 */
	@Override
	public ByteArrayOutputStream printXHTML(InputStream inputStream) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		if (inputStream != null) {
			printXHTML(inputStream, outputStream);
		}

		return outputStream;
	}

	/*
	 * (non-Javadoc)
	 * @see com.idega.block.pdf.business.PrintingService#printXHTML(java.lang.String)
	 */
	@Override
	public ByteArrayOutputStream printXHTML(String source) {
		if (!StringUtil.isEmpty(source)) {
			return printXHTML(IOUtils.toInputStream(source));
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.idega.block.pdf.business.PrintingService#printXHTML(java.lang.String, java.util.Map)
	 */
	@Override
	public ByteArrayOutputStream printXHTML(String source,
			Map<String, String> properties) {
		if (!StringUtil.isEmpty(source)) {
			if (!MapUtil.isEmpty(properties)) {
				for (Entry<String, String> property : properties.entrySet()) {
					source = StringHandler.replace(source,
							"${" + property.getKey() + "}",
							property.getValue());
				}
			}

			return printXHTML(IOUtils.toInputStream(source));
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.idega.block.pdf.business.PrintingService#printXHTML(com.idega.block.pdf.data.DocumentURIEntity, java.util.Map)
	 */
	@Override
	public ByteArrayOutputStream printXHTML(
			DocumentURIEntity entity,
			Map<String, String> properties) {
		if (entity != null) {
			try {
				InputStream inputStream = getRepositoryService()
						.getInputStreamAsRoot(entity.getRepositoryURI());
				if (inputStream != null) {
					return printXHTML(IOUtils.toString(inputStream), properties);
				}
			} catch (Exception e) {
				getLogger().log(Level.WARNING,
						"Failed to get content of file: '" +
								entity.getRepositoryURI() + "' cause of: ", e);
			}
		}

		return null;
	}

	/**
	 * Creates a pdf by transforming an xml template. The given PrintingContext
	 * supplies the necessary resources for the generation
	 */
	@Override
	public DocumentPrinter printDocument(PrintingContext pcx) {
		try {
			Map<?, ?> documentProperties = pcx.getDocumentProperties();
			if (pcx.getBundle() == null) {
				Object o = documentProperties.get(PrintingContext.IW_BUNDLE_ROPERTY_NAME);
				if (o instanceof IWBundle) {
					pcx.setBundle((IWBundle) o);
				}
			}

			InputStream is = pcx.getTemplateStream();

			DocumentPrinter documentPrinter = new DocumentPrinter(is, documentProperties);

			/*TemplateInterpreterFactory tif = new DefaultTemplateInterpreterFactory();
			TemplateInterpreter expi = tif.createTemplateInterpreter();
			expi.(new IWBundleType(expi));
			documentPrinter.setTemplateInterpreter(expi);*/

			File resourceDirectory = pcx.getResourceDirectory();
			if (resourceDirectory != null) {
				documentPrinter.setResourceLoader(new FileResourceLoader(resourceDirectory));

				loadAllResources(pcx.getBundle(), resourceDirectory);
			}

			String resourceURL = pcx.getResourceURL();
			if (resourceURL != null) {
				documentPrinter.setResourceLoader(new HttpResourceLoader(resourceURL));
			}

			OutputStream os = pcx.getDocumentStream();
			documentPrinter.printDocument(os);

			return documentPrinter;
		}
		catch (DocumentHandlerException e) {
			e.printStackTrace();
			throw new IBORuntimeException(e);
		}
		catch (IOException e) {
			throw new IBORuntimeException(e);
		}
	}

	/**
	 * Creates an empty PrintingContext to be filled
	 *
	 * @return
	 */
	@Override
	public PrintingContext createPrintingContext() {
		return new PrintingContextImpl();
	}

	private boolean loadAllResources(IWBundle bundle, File resourceDirectory) {
		if (bundle == null || resourceDirectory == null || !resourceDirectory.exists() || !resourceDirectory.isDirectory()) {
			return false;
		}

		String pathInBundle = resourceDirectory.getAbsolutePath();
		int bundleIdentifierIndex = pathInBundle.indexOf(bundle.getBundleIdentifier());
		if (bundleIdentifierIndex == -1) {
			return false;
		}
		pathInBundle = new StringBuilder(pathInBundle.substring(bundleIdentifierIndex + bundle.getBundleIdentifier().length() + 1)).append(File.separator)
						.toString();

		IWBundleResourceFilter.copyAllFilesFromJarDirectory(IWMainApplication.getDefaultIWMainApplication(), bundle, pathInBundle);

		return true;
	}
}
