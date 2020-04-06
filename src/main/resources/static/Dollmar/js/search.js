/*
 * Few custom search functions - for search specific columns within tables
 */

function searchLibrary() {
	// Declare variables 
	var input, filter, table, tr, td, i, txtValue;
	input = document.getElementById("libraryName");
	filter = input.value.toUpperCase();
	table = document.getElementById("libsTable");
	tr = table.getElementsByTagName("tr");

	// Loop through all table rows, and hide those who don't match the search query
	for (i = 0; i < tr.length; i++) {
		td = tr[i].getElementsByTagName("td")[1]; // the artifact id/library name
		if (td) {
			txtValue = td.textContent || td.innerText;
			if (txtValue.toUpperCase().indexOf(filter) > -1) {
				tr[i].style.display = "";
			} else {
				tr[i].style.display = "none";
			}
		} 
	}
}


function searchApplication() {
	// Declare variables 
	var input, filter, table, tr, td, i, txtValue;
	input = document.getElementById("appName");
	filter = input.value.toUpperCase();
	table = document.getElementById("appsTable");
	tr = table.getElementsByTagName("tr");

	// Loop through all table rows, and hide those who don't match the search query
	for (i = 0; i < tr.length; i++) {
		td = tr[i].getElementsByTagName("td")[1]; // application name
		if (td) {
			txtValue = td.textContent || td.innerText;
			if (txtValue.toUpperCase().indexOf(filter) > -1) {
				tr[i].style.display = "";
			} else {
				tr[i].style.display = "none";
			}
		} 
	}
}


function searchApiDef() {
	// Declare variables 
	var input, filter, table, tr, td, i, txtValue;
	input = document.getElementById("apiName");
	filter = input.value.toUpperCase();
	table = document.getElementById("apisTable");
	tr = table.getElementsByTagName("tr");

	// Loop through all table rows, and hide those who don't match the search query
	for (i = 0; i < tr.length; i++) {
		td = tr[i].getElementsByTagName("td")[1]; // application name
		if (td) {
			txtValue = td.textContent || td.innerText;
			if (txtValue.toUpperCase().indexOf(filter) > -1) {
				tr[i].style.display = "";
			} else {
				tr[i].style.display = "none";
			}
		} 
	}
}

