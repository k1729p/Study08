<!DOCTYPE html>
<HTML lang="en">
<META charset="UTF-8">
<BODY>
<a href="https://github.com/k1729p/Study08/tree/main/docs"><img alt="" src="images/ColorScheme.png" height="25" width="800"/></a>
<h2 id="contents">Study08 README Contents</h2>
<h3 id="top">Research the <a href="https://docs.spring.io/spring-framework/docs/current/reference/html/web.html">Spring Web MVC</a>
with <a href="https://www.thymeleaf.org/index.html">Thymeleaf</a></h3>

<P><img alt="" src="images/MermaidFlowchart.png" height="125" width="515"/><br>
<img alt="" src="images/blackArrowUp.png">
<I>The flowchart with SpringBoot application.</I></P>

<p>
The <a href="https://www.thymeleaf.org/index.html">Thymeleaf</a> is a server-side Java template engine.<br>
The <a href="https://github.com/k1729p/Study08/blob/main/src/main/java/kp/utils/SampleDataset.java">Sample Dataset</a> 
stores the data in the Java object 'Map&lt;Long, Department&gt;'.
</p>

<p>
The sections of this project:
</p>
<OL>
<li><a href="#ONE"><b>Use Cases</b></a></li>
<li><a href="#TWO"><b>SpringBoot Server</b></a></li>
<li><a href="#THREE"><b>Web Browser Client</b></a></li>
</OL>

<p>
Java source code. Packages:<br>
<img alt="" src="images/aquaHR-500.png"><br>
<img alt="" src="images/aquaSquare.png">
    <i>application sources</i>&nbsp;:&nbsp;
	<a href="https://github.com/k1729p/Study08/tree/main/src/main/java/kp">kp</a><br>
<img alt="" src="images/aquaSquare.png">
    <i>test sources</i>&nbsp;:&nbsp;
	<a href="https://github.com/k1729p/Study08/tree/main/src/test/java/kp">kp</a><br>
<img alt="" src="images/aquaHR-500.png">
</p>

<P><img alt="" src="images/MermaidClassDiagram.png" height="485" width="575"/><br>
<img alt="" src="images/blackArrowUp.png">
<I>The domain objects class diagram.</I></P>

<p>
<img alt="" src="images/yellowHR-500.png"><br>
<img alt="" src="images/yellowSquare.png">
    <a href="http://htmlpreview.github.io/?https://github.com/k1729p/Study08/blob/main/docs/apidocs/index.html">
	Java API Documentation</a>&nbsp;●&nbsp;
    <a href="http://htmlpreview.github.io/?https://github.com/k1729p/Study08/blob/main/docs/testapidocs/index.html">
	Java Test API Documentation</a><br>
<img alt="" src="images/yellowHR-500.png">
</p>
<hr>
<h3 id="ONE">❶ Use Cases</H3>

<table style="border:solid">
<tr><td style="border:solid"><B>Company</B></td>
<td style="border:solid">welcome page</td></tr>
<tr><td style="border:solid"><B>Show departments</B></td>
<td style="border:solid">table view of the company's departments and links to its employees</td></tr>
<tr><td style="border:solid"><B>Show employees</B></td>
<td style="border:solid">table view of selected department employees</td></tr>
<tr><td style="border:solid"><B>Edit the existing employee</B></td>
<td style="border:solid">edit the information about an employee</td></tr>
<tr><td style="border:solid"><B>Update the existing employee</B></td>
<td style="border:solid">update the information about an employee</td></tr>
<tr><td style="border:solid"><B>Delete the employee</B></td>
<td style="border:solid">delete an employee from the department</td></tr>
<tr><td style="border:solid"><B>Add a new employee</B></td>
<td style="border:solid">add a new employee to the department</td></tr>
<tr><td style="border:solid"><B>Edit the existing department</B></td>
<td style="border:solid">edit the information about a department</td></tr>
<tr><td style="border:solid"><B>Update the existing department</B></td>
<td style="border:solid">update the information about a department</td></tr>
<tr><td style="border:solid"><B>Delete the department</B></td>
<td style="border:solid">delete existing department (with its employees) from the company</td></tr>
<tr><td style="border:solid"><B>Add a new department</B></td>
<td style="border:solid">add a new department to the company</td></tr>
</table>

<p><img alt="" src="images/uml_use_case_diagram.jpg"/><br>
<img alt="" src="images/blackArrowUp.png">
<i>The use case diagram.</i></p>

<a href="#top">Back to the top of the page</a>
<hr>
<h3 id="TWO">❷ SpringBoot Server</H3>

<p>Action:<br>
<img alt="" src="images/orangeHR-500.png"><br>
<img alt="" src="images/orangeSquare.png"> 1. With batch file 
<a href="https://github.com/k1729p/Study08/blob/main/0_batch/01%20MVN%20clean%20install%20run.bat"> 
<i>"01 MVN clean install run.bat"</i></a> build and start the SpringBoot Server.<br>
<img alt="" src="images/orangeHR-500.png"></p>

<p><img alt="" src="images/greenCircle.png"> 
2.1. Application tests
</p>
<ul>
  <li>The <a href="https://github.com/k1729p/Study08/tree/main/src/test/java/kp/company/client/side">client-side tests</a>. 
  They use <B>TestRestTemplate</B> and the server is started.</li>
  <li>The <a href="https://github.com/k1729p/Study08/tree/main/src/test/java/kp/company/mvc">tests with server-side support</a>. 
  They use <B>MockMvc</B> and the server is not started.</li>
</ul>

<a href="#top">Back to the top of the page</a>
<hr>
<h3 id="THREE">❸ Web Browser Client</H3>

<p>Action:<br>
<img alt="" src="images/orangeHR-500.png"><br>
<img alt="" src="images/orangeSquare.png"> 1. With the URL <a href="http://localhost:8080">http://localhost:8080</a> 
open the home page in the web browser and execute CRUD actions.<br>
<img alt="" src="images/orangeSquare.png"> 2. With batch file <a href="https://github.com/k1729p/Study08/blob/main/0_batch/02%20LYNX%20call%20server.bat"> 
<i>"02 LYNX call server.bat"</i></a> send requests to the web application from the Lynx browser.<br>
<img alt="" src="images/orangeHR-500.png"></p>

<p><img alt="" src="images/greenCircle.png"> 
3.1. Menu "<a href="http://localhost:8080/company">Company</a>". Template file 
<a href="https://github.com/k1729p/Study08/blob/main/src/main/resources/templates/company/home.html">home.html</a>.<br>
The handler GET method:
<a href="https://github.com/k1729p/Study08/blob/main/src/main/java/kp/company/controller/CompanyController.java#L31">
kp.company.controller.CompanyController::company</a>.
</p>

<p><img alt="" src="images/Company.png" height="118" width="530"/><br>
<i>Welcome page of the application. Overview of the company.</i>
</p>

<p><img alt="" src="images/greenCircle.png"> 
3.2. Menu "<a href="http://localhost:8080/listDepartments">Departments</a>". Template files 
<a href="https://github.com/k1729p/Study08/blob/main/src/main/resources/templates/departments/list.html">list.html</a>, 
<a href="https://github.com/k1729p/Study08/blob/main/src/main/resources/templates/departments/edit.html">edit.html</a>, 
<a href="https://github.com/k1729p/Study08/blob/main/src/main/resources/templates/departments/confirmDelete.html">confirmDelete.html</a>.
</p>

<p>
3.2.1. Listing all departments.<br>
The handler GET method: 
<a href="https://github.com/k1729p/Study08/blob/main/src/main/java/kp/company/controller/DepartmentController.java#L57">
kp.company.controller.DepartmentController::listDepartments</a>.
</p><p>
<img alt="" src="images/ListDepartments.png" height="235" width="530"/><br>
<img alt="" src="images/blackArrowUp.png">
<i>Listing all departments.</i>
</p>

<p>
3.2.2. Editing the existing department.<br>
Showing the dialog. The handler GET method: 
<a href="https://github.com/k1729p/Study08/blob/main/src/main/java/kp/company/controller/DepartmentController.java#L87">
kp.company.controller.DepartmentController::startDepartmentEditing</a>.<br>
The 'Save' button. The handler POST method: 
<a href="https://github.com/k1729p/Study08/blob/main/src/main/java/kp/company/controller/DepartmentController.java#L110">
kp.company.controller.DepartmentController::saveDepartment</a>.<br>
The 'Cancel' button. The handler POST method: 
<a href="https://github.com/k1729p/Study08/blob/main/src/main/java/kp/company/controller/DepartmentController.java#L130">
kp.company.controller.DepartmentController::cancelDepartmentEditing</a>.
</p><p>
<img alt="" src="images/EditDepartment.png" height="117" width="515"/><br>
<img alt="" src="images/blackArrowUp.png">
<i>Editing the existing department.</i>
</p>

<p>
3.2.3. Deleting the existing department.<br>
Showing the dialog. The handler GET method: 
<a href="https://github.com/k1729p/Study08/blob/main/src/main/java/kp/company/controller/DepartmentController.java#L144">
kp.company.controller.DepartmentController::startDepartmentDeleting</a>.<br>
The 'Delete' button. The handler POST method: 
<a href="https://github.com/k1729p/Study08/blob/main/src/main/java/kp/company/controller/DepartmentController.java#L166">
kp.company.controller.DepartmentController::deleteDepartment</a>.<br>
The 'Cancel' button. The handler POST method: 
<a href="https://github.com/k1729p/Study08/blob/main/src/main/java/kp/company/controller/DepartmentController.java#L186">
kp.company.controller.DepartmentController::cancelDepartmentDeleting</a>.
</p><p>
<img alt="" src="images/DeleteDepartment.png" height="117" width="516"/><br>
<img alt="" src="images/blackArrowUp.png">
<i>Deleting the existing department.</i>
</p>

<p>
3.2.4. Adding a new department.<br>
Showing the dialog. The handler GET method: 
<a href="https://github.com/k1729p/Study08/blob/main/src/main/java/kp/company/controller/DepartmentController.java#L71">
kp.company.controller.DepartmentController::startDepartmentAdding</a>.<br>
The 'Save' button. The handler POST method: 
<a href="https://github.com/k1729p/Study08/blob/main/src/main/java/kp/company/controller/DepartmentController.java#L110">
kp.company.controller.DepartmentController::saveDepartment</a>.<br>
The 'Cancel' button. The handler POST method: 
<a href="https://github.com/k1729p/Study08/blob/main/src/main/java/kp/company/controller/DepartmentController.java#L130">
kp.company.controller.DepartmentController::cancelDepartmentEditing</a>.
</p><p>
<img alt="" src="images/AddDepartment.png" height="201" width="512"/><br>
<img alt="" src="images/blackArrowUp.png">
<i>Adding a new department. Displayed validation messages.</i>
</p>

<p><img alt="" src="images/greenCircle.png"> 
3.3. Menu "<a href="http://localhost:8080/listEmployees?departmentId=1">Employees</a>". Template files 
<a href="https://github.com/k1729p/Study08/blob/main/src/main/resources/templates/employees/list.html">list.html</a>, 
<a href="https://github.com/k1729p/Study08/blob/main/src/main/resources/templates/employees/edit.html">edit.html</a>, 
<a href="https://github.com/k1729p/Study08/blob/main/src/main/resources/templates/employees/confirmDelete.html">confirmDelete.html</a>.
</p>
<p>
3.3.1. Listing all employees.<br>
The handler GET method: 
<a href="https://github.com/k1729p/Study08/blob/main/src/main/java/kp/company/controller/EmployeeController.java#L74">
kp.company.controller.EmployeeController::listEmployees</a>.
</p><p>
<img alt="" src="images/ListEmployees.png" height="330" width="530"/><br>
<img alt="" src="images/blackArrowUp.png">
<i>Listing all employees of the selected department.</i>
</p>

<p>
3.3.2. Editing the existing employee.<br>
Showing the dialog. The handler GET method: 
<a href="https://github.com/k1729p/Study08/blob/main/src/main/java/kp/company/controller/EmployeeController.java#L121">
kp.company.controller.EmployeeController::startEmployeeEditing</a>.<br>
The 'Save' button. The handler POST method: 
<a href="https://github.com/k1729p/Study08/blob/main/src/main/java/kp/company/controller/EmployeeController.java#L145">
kp.company.controller.EmployeeController::saveEmployee</a>.<br>
The 'Cancel' button. The handler POST method: 
<a href="https://github.com/k1729p/Study08/blob/main/src/main/java/kp/company/controller/EmployeeController.java#L172">
kp.company.controller.EmployeeController::cancelEmployeeEditing</a>.
</p><p>
<img alt="" src="images/EditEmployee.png" height="176" width="514"/><br>
<img alt="" src="images/blackArrowUp.png">
<i>Editing the existing employee.</i>
</p>

<p>
3.3.3. Deleting the existing employee.<br>
Showing the dialog. The handler GET method: 
<a href="https://github.com/k1729p/Study08/blob/main/src/main/java/kp/company/controller/EmployeeController.java#L188">
kp.company.controller.EmployeeController::startEmployeeDeleting</a>.<br>
The 'Delete' button. The handler POST method: 
<a href="https://github.com/k1729p/Study08/blob/main/src/main/java/kp/company/controller/EmployeeController.java#L212">
kp.company.controller.EmployeeController::deleteEmployee</a>.<br>
The 'Cancel' button. The handler POST method: 
<a href="https://github.com/k1729p/Study08/blob/main/src/main/java/kp/company/controller/EmployeeController.java#L233">
kp.company.controller.EmployeeController::cancelEmployeeDeleting</a>.
</p><p>
<img alt="" src="images/DeleteEmployee.png" height="183" width="517"/><br>
<img alt="" src="images/blackArrowUp.png">
<i>Deleting the existing employee.</i>
</p>

<p>
3.3.4. Adding a new employee.<br>
Showing the dialog. The handler GET method: 
<a href="https://github.com/k1729p/Study08/blob/main/src/main/java/kp/company/controller/EmployeeController.java#L97">
kp.company.controller.EmployeeController::startEmployeeAdding</a>.<br>
The 'Save' button. The handler POST method: 
<a href="https://github.com/k1729p/Study08/blob/main/src/main/java/kp/company/controller/EmployeeController.java#L145">
kp.company.controller.EmployeeController::saveEmployee</a>.<br>
The 'Cancel' button. The handler POST method: 
<a href="https://github.com/k1729p/Study08/blob/main/src/main/java/kp/company/controller/EmployeeController.java#L172">
kp.company.controller.EmployeeController::cancelEmployeeEditing</a>.
</p><p>
<img alt="" src="images/AddEmployee.png" height="351" width="517"/><br>
<img alt="" src="images/blackArrowUp.png">
<i>Adding a new employee. Displayed validation messages.</i>
</p>

<a href="#top">Back to the top of the page</a>
</BODY>
</HTML>