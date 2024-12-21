$(document).ready(function() {
	$.fn.select = function() {
		$.ajax({
			type: "POST",
			url: "ClienteControllerCRUD",
			data: {
				'search': $("#txtFiltro").val(),
				'action': 'select'
			},
			dataType: "json",
			beforeSend: function() {
				$("#btnFiltrar").prop('disabled', true);
			}
		})
			.done(function(d) {
				$("#myTable tbody").empty();
				if (d.data && d.data.length > 0) {
					$.each(d.data, function(index, value) {
						$('<tr></tr>').html('<td>' + value.nome + '</td>' +
							'<td>' + value.endereco + '</td>' +
							'<td>' + value.cidade + '</td>' +
							'<td>' + value.fone + '</td>' +
							'<td><button class="btn btn-primary btn-xs btnUpdate" value = "' + value.idCliente + '">'+
                            '<span class = "glyphicon glyphicon-pencil"></span></button>' +
							'<button class = "btn btn-primary btn-xs btnDelete" data-title = "Confirma exclusão?" value = "'+value.idCliente+'"><span class="glyphicon glyphicon-remove"></span></button></td>')
							.appendTo("#myTable tbody");
					});
				}
				else {
					$("<tr></tr>").html(
						"<td colspan=\"6\">Nenhum registro encontrado.</td>")
						.appendTo("#myTable tbody");
				}
			})
			.fail(function() {
				alert("Ocorreu um erro durante o processamento.");
			})
			.always(function(r) {
				$("#btnFiltrar").prop('disabled', false);
			});
	}
	$.fn.insert = function() {
		$("#action").val('insert');
		$.ajax({
			type: "POST",
			url: "ClienteControllerCRUD",
			data: $('#formCliente').serialize(),
			dataType: "json",
			cache: false,
			timeout: 10000,
			beforeSend: function() {
				$('#btnSalvar').prop('disabled', true);
			}
		})
			.done(function(data) {
				if (data.status == true) {
					$("#formResultado").removeClass("alert-danger");
					$("#formResultado").addClass("alert-success");
					$("#alertType").html("Sucesso");
					$.fn.select();
				} else {
					$("#formResultado").removeClass("alert-success");
					$("#formResultado").addClass("alert-danger");
					$("#alertType").html("Erro");
				}
				$("#formMensagem").html(data.message);
				$("#formResultado").slideDown().delay(3000).slideUp(function() {
					$("#formCliente").trigger("reset");
					$('#add_new_record_modal').modal('toggle');
				});
			})
			.fail(function(r) {
				alert("Falha de Conexão!");
			})
			.always(function(r) {
				$('#btnSalvar').prop('disabled', false);
			});
	}
	$.fn.update = function() {
		$('#action').val('update');
		$.ajax({
			type: "POST",
			url: "ClienteControllerCRUD",
			data: $('#formCliente').serialize(),
			dataType: "json",
			cache: false,
			timeout: 10000,
			beforeSend: function() {
				$('#btnSalvar').prop('disabled', true);
			}
		})
			.done(function(data) {
				if (data.status == true) {
					$("#formResultado").removeClass("alert-danger");
					$("#formResultado").addClass("alert-success");
					$("#alertType").html("Sucesso");
					$.fn.select();
				} else {
					$("#formResultado").removeClass("alert-success");
					$("#formResultado").addClass("alert-danger");
					$("#alertType").html("Erro");
				}
				$("#formMensagem").html(data.message);
				$("#formResultado").slideDown().delay(1000).slideUp(function() {
					$("#formCliente").trigger("reset");
					$('#add_new_record_modal').modal('hide');
				});
			})
			.fail(function(r) {
				alert("Falha de Conexão!");
			})
			.always(function(r) {
				$('#btnSalvar').prop('disabled', false);
			});
	}
	$.fn.delete = function(id) {
		$.ajax({
			type: "POST",
			url: "ClienteControllerCRUD",
			data: {
				'id': id,
				'action': 'delete'
			},
			dataType: "json",
			beforeSend: function() { }
		})
			.done(function(d) {
				if (d.status == true) {
					$.fn.select();
				} else {
					alert('Nenhum registro apagado');
				}
			})
			.fail(function() {
				alert("Ocorreu um erro durante o processamento.");
			})
			.always(function(r) { });
	}
	$("#btnFiltrar").click(function() {
		$.fn.select();
	});
	$("#btnNovo").click(function() {
		$('#idCliente').val('');
	});
	$("#btnSalvar").click(function() {
		if ($('#idCliente').val() == '') {
			$.fn.insert();
		} else {
			$.fn.update();
		}
	});
	$('#btnCancelar').click(function() {
		$("#formCliente").trigger("reset");
	});
	$("#myTable").on('click', '.btnUpdate', function() {
		$.ajax({
			type: "POST",
			url: "ClienteControllerSelectID",
			data: {
				'id': $(this).val()
			},
			dataType: "json"
		})
			.done(function(d) {
				$("#formCliente").trigger("reset");
				$('#idCliente').val(d.idCliente);
				$('#nome').val(d.nome);
				$('#endereco').val(d.endereco);
				$('#cidade').val(d.cidade);
				$('#fone').val(d.fone);
				$('#add_new_record_modal').modal('show');
			})
			.fail(function() {
				alert("Ocorreu um erro durante o processamento.");
			});
	});
	$("#myTable").on('click', '.btnDelete', function() {
		if (confirm('Confirma a exclusão?')) {
			$.fn.delete($(this).val());
		}
	});
	$.fn.select();
});