$(document).ready(function() {

  let api = "http://localhost:8080/api/v1/employees";

  let alerts = $("#alerts");

  let employees_section = $("#employees_section");
  let form_section = $("#form_section");
  let profile_section = $("#profile_section");

  let table = employees_section.find("table");

  let form = $("#form");
  let first_name = form.find("#first_name");
  let last_name = form.find("#last_name");
  let email = form.find("#email");
  let save = form.find("#save");

  let id = null, index = null, data = [];

  let validObj = form.validate({
    rules: {
      first_name: {
        required: true,
        minlength: 2
      },
      last_name: {
        required: true,
        minlength: 1
      },
      email: {
        required: true,
        email: true
      }
    }
  });

  let setSection = function(section) {

    alerts.empty();

    let viewName = typeof section == "object" ? section.attr("id") : section;
    let sections = [employees_section, form_section, profile_section];

    $(".section").addClass("d-none");

    sections.forEach(i => {
      if( i.attr('id') === viewName ) {
        i.removeClass("d-none");
      }
    });

    if( viewName === "employees_section" ) {
      loadEmployees();
    }
  };

  let viewSwitch = function(e) {
    e.preventDefault();
    clearForm();
    setSection($(this).attr("data-view"));
  };

  let loadEmployees = function() {
    let tbody = table.find("tbody");
    tbody.empty().append("<tr><td colspan='4' class='text-center'>No Employee Found</td></tr>");
    axios.get(api)
    .then(response => {

      if( Array.isArray(response.data) && response.data.langth > 0 ) {
        
        tbody.empty();

        response.data.forEach((row, i) => {
          tbody.append("<tr> <td>" + (row.firstName || "") + "</td> <td>" + (row.lastName || "") + "</td> <td>" + (row.email || "") + "</td> <td> <button class=\"update-btn btn btn-primary btn-sm\" data-index=\"" + i + "\">Update</button> <button class=\"delete-btn btn btn-danger btn-sm\" data-index=\"" + i + "\">Delete</button> <button class=\"view-btn btn btn-primary btn-sm\" data-index=\"" + i + "\">View</button> </td> </tr>");
        });

        data = response.data;
      }
    });
  };

  let viewEmployee = function() {

    let index_val = $(this).attr("data-index");

    if( index_val ) {
      index = index_val;
    }

    let rowData = data[index];
    id = rowData.id;

    profile_section.find("#first_name").val("");
    profile_section.find("#last_name").val("");
    profile_section.find("#email").val("");

    axios.get(api + "/" + id)
    .then(response => {
      if( response?.data?.id ) {
        profile_section.find("#first_name").html(response?.data?.firstName || "");
        profile_section.find("#last_name").html(response?.data?.lastName || "");
        profile_section.find("#email").html(response?.data?.email || "");
        setSection(profile_section);
      }
    });
  };

  let deleteEmployee = function() {

    let rowData = data[$(this).attr("data-index")];
    id = rowData.id;

    axios.delete(api + "/" + id)
    .then(response => {

      if( response?.data?.status ) {
        appendAlert(response.data.message, response.data.status == "success" ? "success" : "danger");
      }

      loadEmployees();
    });
  };

  let formSubmit = function(e) {
    e.preventDefault();

    if( validObj.form() == false ) {
      return false;
    }

    let params = {
      firstName: first_name.val(),
      lastName: last_name.val(),
      email: email.val()
    };

    if( id ) {
      params.id = id;

      axios.put(api, params)
      .then(function (response) {
        if( response?.data?.status == "success" ) {
          setSection(employees_section);
        }
        appendAlert(response.data.message, response?.data?.status == "success" ? "success" : "danger");
      })
      .catch(function (error) {
        console.log(error);
      });
      
      return;
    }

    axios.post(api, params)
    .then(function (response) {
      console.log(response);
      if( response?.data?.status === "success" ) {
        setSection(employees_section);
      }
      appendAlert(response.data.message, response?.data?.status == "success" ? "success" : "danger");
    })
    .catch(function (error) {
      console.log(error);
    });
  };

  let loadForm = function(e) {

    e.preventDefault();

    let index_val = $(this).attr("data-index");

    if( index_val ) {
      index = index_val;
    }

    let rowData = data[index];
    clearForm();

    id = rowData.id;
    first_name.val(rowData.firstName);
    last_name.val(rowData.lastName);
    email.val(rowData.email);
    setSection(form_section);
  };

  let clearForm = function() {
    id = null;
    first_name.val('');
    last_name.val('');
    email.val('');
  };

  let appendAlert = (message, type) => {
    const wrapper = document.createElement('div');
    wrapper.innerHTML = [
      `<div class="alert alert-${type} alert-dismissible" role="alert">`,
      `   <div>${message}</div>`,
      '   <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>',
      '</div>'
    ].join('');
    alerts.append(wrapper)
  };
  
  setSection(employees_section);
  $(".view_switcher").on("click", viewSwitch);
  save.on("click", formSubmit);

  table.on("click", ".update-btn", loadForm);
  table.on("click", ".view-btn", viewEmployee);
  table.on("click", ".delete-btn", deleteEmployee);

  form_section.on("click", ".view-btn", viewEmployee);
  profile_section.on("click", ".update-btn", loadForm);
});