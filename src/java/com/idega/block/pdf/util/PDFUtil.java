package com.idega.block.pdf.util;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.idega.util.IOUtil;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

public class PDFUtil {


	/**
	 * Merges PDFs
	 *  
	 * @param streamOfPDFFiles - PDF's to be merged
	 * @param outputStream - new merged PDF
	 * @param paginate - Need line numbers?
	 */
	  public static void concatPDFs(List<byte[]> streamOfPDFFiles, OutputStream outputStream, boolean paginate) {

	    Document document = new Document();
	    try {
	      List<byte[]> pdfs = streamOfPDFFiles;
	      List<PdfReader> readers = new ArrayList<PdfReader>();
	      int totalPages = 0;

	      // Create Readers for the pdfs.
	      for (Iterator<byte[]> iteratorPDFs = pdfs.iterator(); iteratorPDFs.hasNext();) {
		      byte[] pdf = iteratorPDFs.next();
		      PdfReader pdfReader = new PdfReader(pdf);
		      readers.add(pdfReader);
		      totalPages += pdfReader.getNumberOfPages();
	      }
	      // Create a writer for the outputstream
	      PdfWriter writer = PdfWriter.getInstance(document, outputStream);

	      document.open();
	      BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
	      PdfContentByte cb = writer.getDirectContent(); // Holds the PDF
	      // data

	      PdfImportedPage page;
	      int currentPageNumber = 0;
	      int pageOfCurrentReaderPDF = 0;

	      // Loop through the PDF files and add to the output.
	      for (Iterator<PdfReader> iteratorPDFReader = readers.iterator(); iteratorPDFReader.hasNext();) {
	    	  PdfReader pdfReader = iteratorPDFReader.next();
	
		        // Create a new page in the target for each source page.
	    	  while (pageOfCurrentReaderPDF < pdfReader.getNumberOfPages()) {
		          document.newPage();
		          pageOfCurrentReaderPDF++;
		          currentPageNumber++;
		          page = writer.getImportedPage(pdfReader, pageOfCurrentReaderPDF);
		          cb.addTemplate(page, 0, 0);

		          // Code for pagination.
		          if (paginate) {
		        	  cb.beginText();
			          cb.setFontAndSize(bf, 9);
			          cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "" + currentPageNumber + " of " + totalPages, 520, 5, 0);
			          cb.endText();
		          }
	    	  }
	    	  pageOfCurrentReaderPDF = 0;
	      }
	      outputStream.flush();
	      document.close();
	      outputStream.close();
	    } catch (Exception e) {
	    	e.printStackTrace();
	    } finally {
	    	if (document.isOpen())
	    		document.close();
	    	}
	    	IOUtil.close(outputStream);
	  }
}