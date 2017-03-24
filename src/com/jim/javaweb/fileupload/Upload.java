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
		    System.out.println("表单参数名:" + item.getFieldName() + "，表单参数值:" + item.getString("UTF-8"));
		} else {
		    if (item.getName() != null && !item.getName().equals("")) {
			System.out.println("上传文件的大小:" + item.getSize());
			System.out.println("上传文件的类型:" + item.getContentType());
			// item.getName()返回上传文件在客户端的完整路径名称
			System.out.println("上传文件的名称:" + item.getName());

			File tempFile = new File(item.getName());

			// 上传文件的保存路径
			File file = new File(sc.getRealPath("/") + "fileupload", tempFile.getName());

			item.write(file);
			request.setAttribute("upload.message", "上传文件成功！");
		    } else {
			request.setAttribute("upload.message", "没有选择上传文件！");
		    }
		}
	    }
	} catch (FileUploadException e) {
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	    request.setAttribute("upload.message", "上传文件失败！");
	}
	// request.getRequestDispatcher("/uploadResult.jsp").forward(request,
	// response);
    }
}
