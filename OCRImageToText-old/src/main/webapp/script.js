$(function() {
	// Variable to store your files
	var files;

	var $jqValue = $('.jqValue');
	var $tableValue = $('.tableValue');
	var imageFileName;
	// Add events
	$('input[type=file]').on('change', prepareUpload);
	/*
	 * $('imagefile').on('change', prepareUpload);
	 * $('imagefile').bind(prepareUpload, function() { hideLoadingScreen(); });
	 */
	$('form').on('submit', uploadFiles);
	$("#imagefile").change(
			function() {
				
				// Check File API support
				if (window.File && window.FileList && window.FileReader) {

					var files = event.target.files; // FileList object
					var output = document.getElementById("result");
					output.value ="";
					for (var i = 0; i < files.length; i++) {
						var file = files[i];
						// Only pics
						if (!file.type.match('image'))
							continue;

						var picReader = new FileReader();
						picReader.addEventListener("load", function(event) {
							var picFile = event.target;
							var div = document.createElement("div");
							div.innerHTML = 		
									" <img width='300' height='400' class='thumbnail' src='"
									+ picFile.result + "'" + "title='"
									+ picFile.name + " '/>";
							output.insertBefore(div, null);
						});
						// Read the imagess
						picReader.readAsDataURL(file);
					}
				} else {
					console.log("Your browser does not support File API");
				}
			});

	// Grab the files and set them to our variable
	function prepareUpload(event) {
		files = event.target.files;

	}

	// Catch the form submit and upload the files
	function uploadFiles(event) {
		event.stopPropagation(); // Stop stuff happening
		event.preventDefault(); // Totally stop stuff happening

		// START A LOADING SPINNER HERE

		// Create a formdata object and add the files
		var data = new FormData();
		var jsondata = {
			"id" : "newmmmmaaaa2",
			"Brand" : "Lego",
			"Age" : "7-14",
			"Warning" : "choking hazard",
			"Raw_Data" : "LEGO MARVEL SUPER HEROES Age 7-14 76031THE HULK BUSTER SMASH 248 pcs/pzs WARNING: CHOKING HAZARD TOY CONTAINS SMALL PARTS AND A SMALL BALL.NOT FOR CHILDREN UNDER 3 YEARS.AVENGERSAGE OF ULTRONNEW SUPER JUMPER",
			"Pieces" : "248"
		};
		$.each(files, function(key, value) {
			data.append("file", value);
		});
		showLoadingScreen();
		$jqValue.html("");
		$tableValue.html("");
		$.ajax({
			url : 'rest/smartOCR/convertImagesToText',
			type : 'POST',
			data : data,
			cache : false,
			// dataType: 'json',
			processData : false, // Don't process the files
			//contentType : 'application/json',
			contentType : false, // Set content type to false as jQuery will
									// tell the server its a query string
									// request

			success : function(data, textStatus, jqXHR) {

				if (typeof data.error === 'undefined') {
					gettableData1(data);
					hideLoadingScreen();
					var requestBody = {
						"id" : "newmmmmaaaa2",
						"text" : data,
						"imageFileName" : imageFileName
					};
					GetTableData(data);
					// Success so call function to process the form
					submitForm(event, data);
					
				} else {
					$jqValue.html("Unable to retrieve text");
					//var tabledata = gettableData(jsondata);
					// Handle errors here
					hideLoadingScreen();
					console.log('ERRORS: ' + data.error);
				}
			},
			error : function(jqXHR, textStatus, errorThrown) {
				$jqValue.html("Unable to retrieve text");
			//var tabledata = gettableData(jsondata);
				// Handle errors here
				console.log('ERRORS: ' + textStatus);
				// STOP LOADING SPINNER
				hideLoadingScreen();
			}
		});
	}
	function gettableData1(jsonObj) {
		var html = '<table border="0" class="ocrTable">';
		$.each(jsonObj, function(key, value) {
			html += '<tr>';
			html += '<td>' + key + '</td>';
			html += '<td>' + value + '</td>';
			html += '</tr>';
		});
		html += '</table>';
		$jqValue	.html(html);
	}

	function GetTableData(requestBody) {
		$.ajax({
			url : 'rest/abzoobaParse/parseText',
			// url: 'http://localhost:8080/abzooba/parseText',
			type : 'POST',
			data : JSON.stringify(requestBody),
			cache : false,
			crossDomain : true,
			// dataType: 'json',
			processData : false, // Don't process the files
			contentType : 'application/json', // Set content type to false as
												// jQuery will tell the server
												// its a query string request
			success : function(data, textStatus, jqXHR) {
				//hideLoadingScreen();
				if (typeof data.error === 'undefined') {

					var tabledata = gettableData(data);
				} else {
					// Handle errors here
					console.log('ERRORS: ' + data.error);
					hideLoadingScreen();
				}
			},
			error : function(jqXHR, textStatus, errorThrown) {
				// Handle errors here
				console.log('ERRORS: ' + textStatus);
				// STOP LOADING SPINNER
				hideLoadingScreen();
			}
		});
	}

	function gettableData(jsonObj) {
		var html = '<table border="0" class="ocrTable">';
		$.each(jsonObj, function(key, value) {
			html += '<tr>';
			html += '<td>' + key + '</td>';
			html += '<td>' + value + '</td>';
			html += '</tr>';
		});
		html += '</table>';
		$tableValue.html(html);
	}

	function submitForm(event, data) {
		// Create a jQuery object from the form
		$form = $(event.target);

		// Serialize the form data
		var formData = $form.serialize();

		// You should sterilise the file names
		$.each(data.files, function(key, value) {
			formData = formData + '&filenames[]=' + value;
		});

		$.ajax({
			url : 'submit.php',
			type : 'POST',
			data : formData,
			cache : false,
			dataType : 'json',
			success : function(data, textStatus, jqXHR) {
				if (typeof data.error === 'undefined') {
					// Success so call function to process the form
					console.log('SUCCESS: ' + data.success);
				} else {
					// Handle errors here
					console.log('ERRORS: ' + data.error);
				}
			},
			error : function(jqXHR, textStatus, errorThrown) {
				// Handle errors here
				console.log('ERRORS: ' + textStatus);
			},
			complete : function() {
				// STOP LOADING SPINNER
			}
		});
	}
});

function showLoadingScreen() {
	$(".loadingScreen").show();
}

function hideLoadingScreen() {
	$(".loadingScreen").hide();
}