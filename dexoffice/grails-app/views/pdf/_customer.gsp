<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
 	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<title>Invoice</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" type="text/css"
	href="${grailsApplication.config.grails.serverURL}/css/style.css"
	media="all" />
<link rel="stylesheet" type="text/css"
	href="${grailsApplication.config.grails.serverURL}/css/print.css"
	media="print" />		
<style>
@media print {
	@page {
		size: 330mm 427mm;
		margin: 14mm;
	}
	.container {
		width: 1170px;
	}
}
</style>
</head>
<body>

	<div class="row">
		<div class="col-sm-6">
			${customer.currentFirstName}
			${customer.currentLastName}
			<br />
			${billingAddress.address1}
			<br />
			${billingAddress.address2}
			<br />
			${billingAddress.city}
			<br />
			${billingAddress.directions}
		</div>

		<div class="col-sm-6">
			Invoice# :
			${invoice.invoiceNumber}
			<br /> Invoice date :
			${invoice.invoiceDate}
			<br />
		</div>
	</div>
	
	<div class="row">
		<div class="col-sm-12">
		&nbsp;
		</div>
	</div>	

	<div class="row">
		<div class="col-sm-12">
			<table class="table table-condensed">
				<thead>
					<tr>
						<th>Description</th>
						<th>Quantity</th>
						<th>Unit price</th>
						<th>Total</th>
						<th></th>
					</tr>
				</thead>
				<tbody>
					<g:each var="item" in="${invoice.items}">
						<tr>
							<td>
								${item.product} <br /> ${item.description}
							</td>

							<td>
								${item.quantity}
							</td>

							<td>
								${item.unitPrice}
							</td>

							<td>
								${item.amount}
							</td>

							<td></td>
						</tr>
					</g:each>
				</tbody>
			</table>
		</div>
	</div>
</body>
</html>
