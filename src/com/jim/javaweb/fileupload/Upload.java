package com.jim.javaweb.fileupload;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * Servlet implementation class Upload
 */
public class Upload extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ServletContext sc;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Upload() {
	super();
	// TODO Auto-generated constructor stub
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
	// TODO Auto-generated method stub
	sc = config.getServletContext();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	// TODO Auto-generated method stub
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	// TODO Auto-generated method stub
	request.setCharacterEncoding("UTF-8");
	DiskFileItemFactory factory = new DiskFileItemFactory();
	factory.setSizeThreshold(2048);
	ServletFileUpload upload = new ServletFileUpload(factory);
	try {
	    UploadProgress uploadProgress = new UploadProgress();
	    System.out.println("begin thread");
	    MyThread myThread = new MyThread(uploadProgress);
	    myThread.start();
	    upload.setProgressListener(uploadProgress);
	    List items = upload.parseRequest(request);
	    Iterator itr = items.iterator();
	    System.out.println("items count:" + items.size());
	    while (itr.hasNext()) {
		FileItem item = (FileItem) itr.next();
		if (item.isFormField()) {
		    System.out.println("��������:" + item.getFieldName() + "��������ֵ:" + item.getString("UTF-8"));
		} else {
		    if (item.getName() != null && !item.getName().equals("")) {
			System.out.println("�ϴ��ļ��Ĵ�С:" + item.getSize());
			System.out.println("�ϴ��ļ�������:" + item.getContentType());
			// item.getName()�����ϴ��ļ��ڿͻ��˵�����·������
			System.out.println("�ϴ��ļ�������:" + item.getName());

			File tempFile = new File(item.getName());

			// �ϴ��ļ��ı���·��
			File file = new File(sc.getRealPath("/") + "fileupload", tempFile.getName());

			item.write(file);
			request.setAttribute("upload.message", "�ϴ��ļ��ɹ���");
		    } else {
			request.setAttribute("upload.message", "û��ѡ���ϴ��ļ���");
		    }
		}
	    }
	} catch (FileUploadException e) {
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    request.setAttribute("upload.message", "�ϴ��ļ�ʧ�ܣ�");
	}
	// request.getRequestDispatcher("/uploadResult.jsp").forward(request,
	// response);
    }
}
