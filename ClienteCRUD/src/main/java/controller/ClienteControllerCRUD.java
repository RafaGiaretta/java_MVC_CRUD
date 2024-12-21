package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import entities.Cliente;
import model.ClienteModelCRUD;

@WebServlet("/ClienteControllerCRUD")
public class ClienteControllerCRUD extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ClienteControllerCRUD() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json;charset=UTF-8");
		if (request.getParameter("action") != null) {
			String action = request.getParameter("action");
			switch (action) {
			case "select":
				select(request, response);
				break;
			case "insert":
				insert(request, response);
				break;
			case "update":
				update(request, response);
				break;
			case "delete":
				delete(request, response);
				break;
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	private void select(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String search = request.getParameter("search");
		ClienteModelCRUD c = new ClienteModelCRUD();
		List<Cliente> clientes = c.select(search);

		JSONArray jsonArray = new JSONArray();
		for (Cliente cliente : clientes) {
			JSONObject jsonObject = new JSONObject(cliente);
			jsonArray.put(jsonObject);
		}
		JSONObject data = new JSONObject();
		data.put("data", jsonArray);
		response.getWriter().append(data.toString());
	}

	private void insert(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Cliente cliente = new Cliente();
		cliente.setNome(request.getParameter("nome"));
		cliente.setEndereco(request.getParameter("endereco"));
		cliente.setCidade(request.getParameter("cidade"));
		cliente.setFone(request.getParameter("fone"));
		ClienteModelCRUD c = new ClienteModelCRUD();
		c.insert(cliente);
		JSONObject jsonObject = new JSONObject();
		if (cliente.getIdCliente() != 0) {
			jsonObject.put("status", true);
			jsonObject.put("message", "Sucesso ao inserir registro!");
			jsonObject.put("id", cliente.getIdCliente());
		} else {
			jsonObject.put("status", false);
			jsonObject.put("message", "Falha ao inserir registro!");
			jsonObject.put("id", 0);
		}
		response.getWriter().append(jsonObject.toString());
	}

	private void update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Cliente cliente = new Cliente();
		cliente.setIdCliente(Integer.parseInt(request.getParameter("idCliente")));
		cliente.setNome(request.getParameter("nome"));
		cliente.setEndereco(request.getParameter("endereco"));
		cliente.setCidade(request.getParameter("cidade"));
		cliente.setFone(request.getParameter("fone"));
		ClienteModelCRUD c = new ClienteModelCRUD();
		c.update(cliente);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("status", true);
		jsonObject.put("message", "Sucesso ao alterar registro!");
		response.getWriter().append(jsonObject.toString());
	}

	private void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ClienteModelCRUD c = new ClienteModelCRUD();
		c.delete(Integer.parseInt(request.getParameter("id")));
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("status", true);
		response.getWriter().append(jsonObject.toString());

	}
} // fim do servlet ClienteControllerCRUD
