// function loadContent(page,url){
//     const contentDiv = document.getElementById('content');
//     let content = "";
    
//     switch(page){
//         case 'prescription':
//             content = "";
//             break;
//         case 'settings':
//             content = "";
//             break;
//         case 'profile':
//             content = "";
//             break;
//         case 'addreport':
//             content = "";
//             break;
//         default:
//             content = "";
//             break;
//     }
//     contentDiv.innerHTML = content;
//     history.pushState({page},page,`${url}`);
// }

// window.onload = function(){
//     var i = 0;
//     var j = 0;
//     while(i < window.location.pathname.length){
//         if (window.location.pathname[i] == '/'){
//             j = i;
//         }
//         ++i;
//     }
//     const currentPage = window.location.pathname.substring(j);
//     loadContent(currentPage);
// }
// window.onpopstate = function (event) {
//     const page = event.state?.page;
//     loadContent(page);
// };

// $(document).ready(function () {
//     $("#patientSearchBtn").on("click", function () {
//         const patientID = $("#patientSearch").val().trim();  // Get the patient ID

//         if (!patientID) {
//             alert("Please enter a patient ID.");
//             return;
//         }

//         $.ajax({
//             url: `/api/doctor/searchPatient?patientID=${patientID}`,
//             type: 'GET',
//             success: function (response) {
//                 if (response.error) {
//                     // If no patient is found
//                     $("#patientDetails").hide();
//                     $("#errorMessage").show();
//                 } else {
//                     // If patient details are found
//                     $("#errorMessage").hide();
//                     $("#patientDetails").show();
//                     $("#patientID").text(response.patientID);
//                     $("#patientName").text(response.patientName);
//                     $("#patientAge").text(response.age);
//                     $("#patientGender").text(response.gender);
//                     $("#patientContact").text(response.contact);
//                 }
//             },
//             error: function (xhr, status, error) {
//                 alert("Error: " + xhr.responseText);
//             }
//         });
//     });
// });

$(document).ready(function () {
        // Save settings with AJAX
        $("#patientSearchBtn").on("click", function (event) {
            event.preventDefault(); // Prevent form submission
            const patientID = $("#patientSearch").val().trim();
            if (!patientID) {
                alert("Please enter a Patient ID.");
                return;
            }

            $.ajax({
                url: '/doctor/patient/searchPatient',
                type: 'POST',
                data: { patientID: patientID },
                success: function (response) {
                    console.log(response.gender);
                    $("#patientDetails").css("display","block");



                    // Update profile picture in sidebar dynamically
                    
                    
                    $("#gender").text(response.gender);
                    $("#patientName").text(response.patientName);
                    $("#contact").text(response.contact);
                    $("#age").text(response.age);
                },
                error: function (xhr, status, error) {
                    $("#responseMessage").text("Update failed: " + xhr.responseText);
                }
            });
        });
    });