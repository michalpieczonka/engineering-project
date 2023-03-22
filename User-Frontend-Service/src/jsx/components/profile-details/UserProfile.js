import React, { Fragment, useState,  useEffect } from "react";
import { Link } from "react-router-dom";
//** Import Image */ 
import { Button, Modal, Table } from "react-bootstrap";
import Select from "react-select";
import {store} from '../../../store/store'
import {DatePicker } from "@y0c/react-datepicker";
import PageTitle from "../../layouts/PageTitle";
import {getCurrentTrainingPlan, setCurrentUserTrianingPlan, getAllUserTrainingPlans} from "../../../services/TrainingPlanService";
import { getUserExtendedProfileDetails, updateUserDetails, updateUserHealthDetails, changeUserPassword, addUserphoto } from "../../../services/UserDetailsService";
import UserPhotoTable from "./UserPhotoTable";
import Paginations from "../Dashboard/Paginations";
import { mapPlanPriority } from "../workout-create/CommonMappers";

import dumbells_icon_s from "../../../images/plans/dumbells_icon_s.png";

const UserProfile = () => {

   const state = store.getState();
   let userId = state.auth.auth.userId;

   const [activeToggle, setActiveToggle] = useState("userDetails");

   const options = {
      settings: {
         overlayColor: "#000000",
      },
   };

   const[currentPage, setCurrentPage] = useState(1);
   const[photosPerPage, setPhotosPerPage] = useState(4);
 


   const [userDetailsEditable, setUserDetailsEditable] = useState(false)
   const [email, setEmail] = useState('')
   const [userName, setUsername] = useState('')
   const [dateOfBirth, setDateOfBirth] = useState(new Date())
   const [notificationsEnabled, setNotificationsEnabled] = useState(false)
   const [gender, setGender] = useState('')
   const [loaded, setLoaded] = useState(false)
   const [refresh, setRefresh] = useState(1)
   const [changePasswordModal, setChangePasswordModal] = useState(false)
   const [oldPassword, setOldPassword] = useState('')
   const [newPassword, setNewPassword] = useState('')

   const [healthDetailsEditable, setHealthDetailsEditable] = useState(false)
   const [healthDetails, setHealthDetails] = useState('')
   let genderOptions = 
   [
      {value: 'MALE', label: "Mężczyzna"},
      {value: 'FEMALE', label: "Kobieta"} 
   ]

   const [file, setFile] = useState(null)
   const onFileChange = e => {
      setFile(e.target.files[0])
   }

   const onFileUpload = () => {
      const formData = new FormData();
      formData.append('file', file)
      formData.append('name', file.name)
      formData.append('type', file.type)
      swal({
         title: "Dodawanie zdjęcia",
         text: "Czy napewno chcesz zapisać zmiany i dodać zdjęcie ?",
         icon: "warning",
         buttons: [
           'Nie, anuluj',
           'Tak, dodaj'
         ],
         dangerMode: false,
       }).then(function(isConfirm) {
         if (isConfirm) {
            addUserphoto(userId, formData)
            .then(response => {
               if(response.status == 201){
                  swal('Sukces', 'Zdjęcie zostało dodane !', "success");
                  setRefresh((prevState) => prevState+1)
               } else {
                  swal('Błąd', 'Nie udało się dodać zdjęcia, wystąpił błąd.', "error");
               }
               
            })
            .catch(error =>{
               swal('Błąd', 'Wystąpił błąd w trakcie dodawania zdjęcia', "error");
               console.log(error)})

         } 
       });

   }


   const [currentUpdatedPlan, setCurrentUpdatedPlan] = useState('')
   const[currentTrainingPlan, setCurrentTrainingPlan] = useState({});
   const [showChangePlanModal, setShowChangePlanModal] = useState(false);
   const [planRefresh, setPlanRefresh] = useState('loading')
   const [trainingPlans, setTrainingPlans] = useState([])
   const [hasCurrent, setHasCurrent] = useState(false)
   const updateCurrentTrainingPlan = () => {
      setShowChangePlanModal(false)
      setCurrentUserTrianingPlan(userId, currentUpdatedPlan.value)
      .then(response => {
         if(response.status === 200){
            swal('Sukces', 'Aktualny plan treningowy został zmieniony !', "success");
            setRefresh((prevState) => prevState+1)
         } else {
            swal('Błąd', 'Nie udało się zmienić aktualnie wykonywanego planu treningowego, wystąpił błąd.', "error");
         }
         
      })
      .catch(error =>{
         swal('Błąd', 'Wystąpił błąd w trakcie zmiany planu treningowego.', "error");
         console.log(error)})
   }

   useEffect(() => { 
      getCurrentTrainingPlan(userId).then((response) => {
         let tmp = response.data
         if(tmp.id != 0){
            setHasCurrent(true)
            let plan = {
               id: tmp.id,
               title: tmp.title,
               description: tmp.planDescription,
               numberOfTrainingDays: tmp.numberOfTrainingDays,
               planPriority: tmp.planPriority,
               planType: tmp.planType,
            }
            setCurrentTrainingPlan(plan)
         }
      }).catch(error => console.log(error))
     }, [planRefresh]); 

   const handleChangeCurrentTrainingPlan = e => {
      let tmp = {value: e.value, label: e.label}
      setCurrentUpdatedPlan(tmp)
      console.log(currentUpdatedPlan)
   }
     useEffect(() => { 
      getAllUserTrainingPlans(userId).then((response) => {
         let tmp = []
         const allTrainingPlans = response.data.map(x => tmp.push({id: x.id, title: x.title, description: x.description, numberOfTrainingDays: x.numberOfTrainingDays, planPriority: x.planPriority, isPublic: x.isPublic, planType: x.planType}))
         setTrainingPlans(tmp)
         setPlanRefresh('loaded')
      }).catch(error => swal("Błąd", "Nie udało się załadować Twoich planów treningowych", "error"))
     }, []);


   const [userDetailsLoaded, setUserDetailsLoaded] = useState(false)
   const [userDetails, setUserDetails] = useState({})
   useEffect(() => { 
      getUserExtendedProfileDetails(userId).then((response) => {
         let tmp = response.data
         let userD = {
            userId: tmp.userId,
            username: tmp.username,
            email: tmp.email,
            gender: genderOptions.find(g => g.value === tmp.gender),
            registrationDate: tmp.registrationDateTime,
            dateOfBirth: tmp.dateOfBirth,
            currentTrainingPlanId: tmp.currentTrainingPlanId,
            notificationsEnabled: tmp.notificationsEnabled,
            userPhotos: tmp.userPhotos.map(photo => ({photoId: photo.photoId, name: photo.name, type: photo.type, data: photo.imageData, uploadedTime: new Date(photo.uploadedTime).toLocaleDateString()})),
            healthDetails:{weight: tmp.healthDetails.weight, height: tmp.healthDetails.height, waistCircuit: tmp.healthDetails.waistCircuit, waistCircumference: tmp.healthDetails.waistCircumference, 
               armCircumference: tmp.healthDetails.armCircumference, thighCircumference: tmp.healthDetails.thighCircumference, latestUpdate: tmp.healthDetails?.latestUpdatedTime, trainingStartDate: tmp.healthDetails?.trainingStartDate}
         }
         setUserDetails(userD)
         console.log(userD)

         setEmail(userD.email)
         setUsername(userD.username)
         setDateOfBirth(new Date(Date.parse(userD.dateOfBirth)))
         setNotificationsEnabled(userD.notificationsEnabled)
         setGender(userD.gender)
         setHealthDetails(userD.healthDetails)
         setLoaded(true)
      
         }).catch(error => swal("Błąd", "Nie udało się załadować szczegółów konta użytkownika", "error"))
     }, [refresh]); 


     const indexOfLastPhoto = currentPage * photosPerPage;
     const indexOfFirstPhoto = indexOfLastPhoto - photosPerPage;
     const currentPhotos = userDetails?.userPhotos?.slice(indexOfFirstPhoto, indexOfLastPhoto);

     const hangleChangeUpdatePassword = () => {
      swal({
         title: "Zmiana hasła",
         text: "Czy napewno chcesz zmienić hasło ?",
         icon: "warning",
         buttons: [
           'Nie, anuluj',
           'Tak, zmień'
         ],
         dangerMode: false,
       }).then(function(isConfirm) {
         if (isConfirm) {
            setChangePasswordModal(false)
            let changePasswordRequest = {
               oldPassword: oldPassword,
               newPassword: newPassword
            }

            changeUserPassword(userId, changePasswordRequest)
            .then(response => {
               if(response.status == 200){
                  swal('Sukces', 'Szczegóły hasło zostało zmienione !', "success");
               } else {
                  swal('Błąd', 'Aktualne hasło nie jest poprawne !.', "error");
               }
               
            })
            .catch(error =>{
               swal('Błąd', 'Wystąpił błąd w trakcie zmiany hasła', "error");
               console.log(error)})

         } 
       });
     }
     const handleNotificationEnabled = () => {
      setNotificationsEnabled(!notificationsEnabled)
     }

     const handleSaveUserDetails = () => {
      swal({
         title: "Aktualizacja profilu",
         text: "Czy napewno chcesz zapisać zmiany i zaktualizować swój profil ?",
         icon: "warning",
         buttons: [
           'Nie, anuluj',
           'Tak, zapisz zmiany'
         ],
         dangerMode: false,
       }).then(function(isConfirm) {
         if (isConfirm) {
            setUserDetailsEditable(false)
            let updatedDetails = {
               userName: userName,
               email: email,
               gender: gender.value,
               dateOfBirth: dateOfBirth.toISOString(),
               notificationsEnabled: notificationsEnabled
            }

            updateUserDetails(userId, updatedDetails)
            .then(response => {
               if(response.status == 200){
                  swal('Sukces', 'Szczegóły profilu zostały zaktualizowane !', "success");
                  setRefresh((prevState) => prevState+1)
               } else {
                  swal('Błąd', 'Nie udało się zaktualizować szczegółów profilu, wystąpił błąd.', "error");
               }
               
            })
            .catch(error =>{
               swal('Błąd', 'Wystąpił błąd w trakcie aktualizacji szczegółów profilu', "error");
               console.log(error)})

         } 
       });
     }

     const handleSaveHealthDetails = () => {
      swal({
         title: "Aktualizacja danych zdrowotnych",
         text: "Czy napewno chcesz zapisać zmiany i zaktualizować swoje dane ?",
         icon: "warning",
         buttons: [
           'Nie, anuluj',
           'Tak, zapisz zmiany'
         ],
         dangerMode: false,
       }).then(function(isConfirm) {
         if (isConfirm) {
            setHealthDetailsEditable(false)
            console.log(healthDetails)
            updateUserHealthDetails(userId, healthDetails)
            .then(response => {
               if(response.status == 204){
                  swal('Sukces', 'Dane zostały zaktualizowane !', "success");
                  setRefresh((prevState) => prevState+1)
               } else {
                  swal('Błąd', 'Nie udało się zaktualizować danych, wystąpił błąd.', "error");
               }
               
            })
            .catch(error =>{
               swal('Błąd', 'Wystąpił błąd w trakcie aktualizacji danych ', "error");
               console.log(error)})

         } 
       });

     }
     const paginate = (pageNumber) => setCurrentPage(pageNumber)

     const tableRefresher = () => {
      setRefresh((prevState) => prevState+1)
     }

   return userDetails ===  null ? null : (
      <Fragment>
         <PageTitle activeMenu="Szczegóły konta" motherMenu="Konto" />

         <Modal className="fade" show={changePasswordModal}>
                           <Modal.Header>
                              <Modal.Title>Zmiana hasła</Modal.Title>
                              <Button
                                 variant=""
                                 className="close"
                                 onClick={() => setChangePasswordModal(false)}
                              >
                                 <span>&times;</span>
                              </Button>
                           </Modal.Header>

                           <Modal.Body> 

                           <div className="col-lg-12 mb-2">
          <div className="form-group">
            <center><h4> Wprowadz aktualne i nowe hasło </h4>
                                          <div className="form-group col-md-8">
                                                <label>Aktualne hasło</label>
                        
                                                <input
                                                   type="password"
                                                   placeholder="Password"
                                                   className="form-control"
                                                   onChange = {(e) => setOldPassword(e.target.value)}
                                                /> 
                                             </div> 

<br />
<div className="form-group col-md-8">
                                                <label>Nowe hasło</label>
                        
                                                <input
                                                   type="password"
                                                   placeholder="Password"
                                                   className="form-control"
                                                   onChange= {(e) => setNewPassword(e.target.value)}
                                                /> 
                                             </div> 
            
          
          <br />
          <br />
  
   </center>
          </div>
       </div>
                           </Modal.Body>

                           <Modal.Footer>
                              <Button
                                 onClick={() => setChangePasswordModal(false)}
                                 variant="danger light"
                              >
                                 Zamknij
                              </Button>
                              <Button  onClick={() => hangleChangeUpdatePassword()} variant="primary">Zmień hasło</Button>
                           </Modal.Footer>
                        </Modal>

         <div className="row">
            <div className="col-lg-12">
               <div className="profile card card-body px-3 pt-3 pb-0">
                  <div className="profile-head">
         
                     <div className="profile-info">
                        <div className="profile-details">
                           <div className="profile-name px-3 pt-2">
                              <h4 className="text-primary mb-0">
                                 {userDetails.username}
                              </h4>
                              <p>Nazwa użytkownika</p>
                           </div>
                           <div className="profile-email px-2 pt-2">
                              <h4 className="text-muted mb-0">
                                 {userDetails.email}
                              </h4>
                              <p>Email</p>
                           </div>
                        </div>
                     </div>
                  </div>
               </div>
            </div>
         </div>
         <div className="row">
            <div className="col-xl-4">
               <div className="card">
                  <div className="card-body">

                     
                        
                  <div className="card-header pb-0 border-0">
                           <div className="mr-auto pr-3">
                              <h4 className="text-black fs-20">
                                 Aktualnie wykonywany plan treningowy
                              </h4>
                           </div>

                        </div>
                        <Modal className="fade" show={showChangePlanModal}>
                           <Modal.Header>
                              <Modal.Title>Zmiana aktualnego planu treningowego</Modal.Title>
                              <Button
                                 variant=""
                                 className="close"
                                 onClick={() => setShowChangePlanModal(false)}
                              >
                                 <span>&times;</span>
                              </Button>
                           </Modal.Header>

                           <Modal.Body> 

                           <div className="col-lg-12 mb-2">
          <div className="form-group">
          <h3> Wybierz nowy plan treningowy </h3>
          <label className="text-label">Aktualnie wykonywany plan treningowy: <br /> <b>Id: {currentTrainingPlan.id}</b>, Tytuł: <b>{currentTrainingPlan.title}</b> </label>
          
          <Select                 
                        onChange={e => handleChangeCurrentTrainingPlan(e)}
                        value = {currentUpdatedPlan}
                        options={trainingPlans.map(plan => ({value: plan.id, label: plan.title}))}
                        style={{
                           lineHeight: "40px",
                           color: "#7e7e7e",
                           paddingLeft: " 15px",
                        }}  />
          </div>
       </div>
                           </Modal.Body>

                           <Modal.Footer>
                              <Button
                                 onClick={() => setShowChangePlanModal(false)}
                                 variant="danger light"
                              >
                                 Zamknij
                              </Button>
                              <Button  onClick={() => updateCurrentTrainingPlan()} variant="primary">Zapisz zmiany</Button>
                           </Modal.Footer>
                        </Modal>

                       {hasCurrent && <div className="card-body">
                           <div className="media mb-3">
                              <Link to={"/training-plan/"+currentTrainingPlan.id}>
                                 <img
                                    src={dumbells_icon_s}
                                    alt=""
                                    className="rounded mr-3"
                                    width={86}
                                 />
                              </Link>
                              <div className="media-body">
                                 <h6 className="fs-16 font-w500">
                                    <Link
                                       to={"/training-plan/"+currentTrainingPlan.id}
                                       className="text-black"
                                    >
                                       {currentTrainingPlan.title}
                                    </Link>
                                 </h6>
                                 <span className="fs-14">{currentTrainingPlan.description}</span>
                              </div>
                           </div>
                           <ul className="m-md-auto mt-2 pr-4">
                              <li className="mb-2 text-nowrap">
                              <i class="fa fa-list-ol" aria-hidden="true"></i>
                                 <span className="fs-14 text-black text-nowrap font-w500">
                                   Dni treningowych {currentTrainingPlan.numberOfTrainingDays}
                                 </span>
                              </li>
                              <li className="mb-2 text-nowrap">
                              <i class="fa fa-hand-o-right" aria-hidden="true"></i>
                                 <span className="fs-14 text-black text-nowrap font-w500">
                                   Typ planu {currentTrainingPlan.planType}
                                 </span>
                              </li>
                                              <li className="text-nowrap">
                                       <i
                                          className="fa fa-star-o mr-3 scale5 text-warning"
                                          aria-hidden="true"
                                       />
                                       <span className="text-nowrap fs-14 text-black font-w500">
                                          Priorytet: {mapPlanPriority(currentTrainingPlan.planPriority)} 
                                       </span>
                                    </li>

                           </ul>
                           <br />

                           <Button className="m-sm-auto mt-2 d-flex align-items-center justify-content-center btn" variant="primary">
                           <Link to={"/training-plan/"+currentTrainingPlan.id}> Wyświetl szczegóły</Link>
                        </Button>
                        < br />
                             <Button
                           variant="primary"
                           className="m-sm-auto mt-2 d-flex align-items-center justify-content-center btn"
                           onClick={() => setShowChangePlanModal(true)}
                        >
                           Zmień plan
                        </Button>
                        </div>}

                        {!hasCurrent && <div className="card-body">
                           <div className="media mb-3">

                              <div className="media-body">
                                 <h6 className="fs-16 font-w500">
         
                                       Nie posiadasz aktualnie zdefiniowanego planu treiningowego
                             
                                 </h6>
                                 <span className="fs-14">{currentTrainingPlan.description}</span>
                              </div>
                           </div>
                  
                           <Button className="m-sm-auto mt-2 d-flex align-items-center justify-content-center btn" variant="outline-primary"
                            onClick={() => setShowChangePlanModal(true)}>
                            Ustaw plan treningowy
                        </Button>
                        </div>}
                     
                    
   
                  </div>
               </div>
            </div>
            <div className="col-xl-8">
               <div className="card">
                  <div className="card-body">
                     <div className="profile-tab">
                        <div className="custom-tab-1">
                           <ul className="nav nav-tabs">
                              <li
                                 className="nav-item"
                                 onClick={() => setActiveToggle("userDetails")}
                              >
                                 <Link
                                    to="#user-details"
                                    data-toggle="tab"
                                    className={`nav-link ${
                                       activeToggle === "userDetails"
                                          ? "active show"
                                          : ""
                                    }`}
                                 >
                                    Ustawienia danych zdrowotnych
                                 </Link>
                              </li>
                              <li className="nav-item">
                                 <Link
                                    to="#profile-settings"
                                    data-toggle="tab"
                                    onClick={() => setActiveToggle("setting")}
                                    className={`nav-link ${
                                       activeToggle === "setting"
                                          ? "active show"
                                          : ""
                                    }`}
                                 >
                                    Ustawienia szczegółow konta użytkownika
                                 </Link>
                              </li>
                           </ul>
                           <div className="tab-content">
            
                              <div
                                 id="userDetails"
                                 className={`tab-pane fade ${
                                    activeToggle === "userDetails"
                                       ? "active show"
                                       : ""
                                 }`}
                              >
                                 <div className="profile-about-me">
      
                                 </div>
                                
                                 <div className="profile-personal-info">
  
 
                                    <div className="pt-3">
                                    <div className="settings-form">
                                       <h4 className="text-primary">
                                          Szczegóły danych zdrowotnych - pomiary obwodów ciała
                                       </h4>
                              
                                       <form
                                          onSubmit={(e) => e.preventDefault()}
                                       >
                                          <div className="form-row">
                                             <div className="form-group col-md-6">
                                                <label>Waga [kg]</label>
                                                <input
                                                disabled = {!healthDetailsEditable}
                                                value = {healthDetails.weight}
                                                type="text"
                                                name="weight"
                                                className="form-control"
                                                onChange =  {(e) => setHealthDetails({...healthDetails, weight: e.target.value})}
                                                />
                                             </div>
                                             <div className="form-group col-md-6">
                                                <label>Wzrost [cm]</label>
                                                <input
                                                disabled = {!healthDetailsEditable}
                                                value = {healthDetails.height}
                                                type="text"
                                                name="height"
                                                   className="form-control"
                                                   onChange =  {(e) => setHealthDetails({...healthDetails, height: e.target.value})}
                                                />
                                             </div>  
                                          </div>

                                          <div className="form-row">
                                             <div className="form-group col-md-6">
                                                <label>Obwód pasa</label>
                                                <input
                                                disabled = {!healthDetailsEditable}
                                                value = {healthDetails.waistCircuit}
                                                type="text"
                                                name="waistCircuit"
                                                className="form-control"
                                                onChange =  {(e) => setHealthDetails({...healthDetails, waistCircuit: e.target.value})}
                                                />
                                             </div>
                                             <div className="form-group col-md-6">
                                                <label>Obwód talii</label>
                                                <input
                                                disabled = {!healthDetailsEditable}
                                                value = {healthDetails.waistCircumference}
                                                name = "waistCircumference"
                                                   type="text"
                                                   className="form-control"
                                                   onChange =  {(e) => setHealthDetails({...healthDetails, waistCircumference: e.target.value})}
                                                />
                                             </div>  
                                          </div>

                                          <div className="form-row">
                                             <div className="form-group col-md-6">
                                                <label>Obwód ramienia</label>
                                                <input
                                                disabled = {!healthDetailsEditable}
                                                value = {healthDetails.armCircumference}
                                                type="text"
                                                name="armCircumference"
                                                className="form-control"
                                                onChange =  {(e) =>  setHealthDetails({...healthDetails, armCircumference: e.target.value})}
                                                />
                                             </div>
                                             <div className="form-group col-md-6">
                                                <label>Obwód uda</label>
                                                <input
                                                disabled = {!healthDetailsEditable}
                                                value = {healthDetails.thighCircumference}
                                                name = "thighCircumference"
                                                   type="text"
                                                   className="form-control"
                                                   onChange =  {(e) => setHealthDetails({...healthDetails, thighCircumference: e.target.value})}
                                                />
                                             </div>  
                                          </div>
                                          {/* <div className="form-group col-md-6">
                                                <label>Data rozpoczęcia treningów</label>
                                              {loaded &&  <DatePicker disabled= {!healthDetailsEditable} selected={new Date(healthDetails.trainingStartDate).toLocaleDateString()} 
                                              onChange={date => setHealthDetails({...healthDetails, trainingStartDate: date.$d.toLocaleDateString()})} />}
                                         

                                          </div> */}
                                          
                                        
                        

                                          <div className="row mb-2">
                                       <div className="col-sm-3">
                                          <h5 className="f-w-500">
                                            Data aktualizacji
                       
                                          </h5>
                                       </div>
                                       <div className="col-sm-9">
                                          <span> 
                                            {loaded && new Date(healthDetails.latestUpdate).toLocaleDateString()}r.
                                          </span>
                                       </div>
                                    </div>

                                         {healthDetailsEditable && <Button className="m-sm-right mt-2 d-flex align-items-right justify-content-center btn" variant="outline-primary" onClick = {() => {handleSaveHealthDetails()}}>
                               Zapisz zmiany
                        </Button>}

                        {!healthDetailsEditable && <Button className="m-sm-right mt-2 d-flex align-items-right justify-content-center btn" variant="outline-primary" onClick = {() => {setHealthDetailsEditable(true)}}>
                               Edytuj dane
                        </Button>}
                                       </form>
                                    </div>
                                 </div>
                                 <br />


   {(loaded && userDetails?.userPhotos?.length > 0) && <h4 className="text">
   Twoje ostatnie zdjęcia sylwetki
</h4> }
{(loaded && userDetails?.userPhotos?.length > 0) &&
<Table responsive>
<thead>
<tr>
<th>
<strong>Data dodania</strong>
</th>
<th>
<strong>Nazwa Pliku</strong>
</th>
<th>
<center>Akcje</center>
</th>
</tr>
</thead>
{loaded &&
<UserPhotoTable photos={currentPhotos} workoutsLoaded={loaded} refresh = {tableRefresher}/> } 
<Paginations workoutsPerPage = {photosPerPage} totalWorkouts={userDetails?.userPhotos?.length} paginate={paginate} />

</Table>}

{(loaded && !userDetails?.userPhotos?.length > 0) && <h4 className="text">
   Twoje ostatnie zdjęcia sylwetki
</h4> }

{(loaded && !userDetails?.userPhotos?.length > 0) && 
<p className="mb-0">
                          Nie posiadasz dodanych żadnych zdjęc sylwetki <br /> Dodaj zdjęcia do swojego profilu aby móc śledzić swoje postępy.
                        </p>
 }

<br />
<h4 className="text">
  Dodaj nowe zdjęcie
</h4>                          
 <input  className="form-control" type="file" onChange={e => onFileChange(e)} />
{
   file && <Button className="m-sm-right mt-2 d-flex align-items-right justify-content-center btn" variant="outline-primary" onClick = {() => onFileUpload()}>
   Zapisz plik
</Button>
}

   
                                 </div>
                              </div>
                              <div
                                 id="profile-settings"
                                 className={`tab-pane fade ${
                                    activeToggle === "setting"
                                       ? "active show"
                                       : ""
                                 }`}
                              >
                                 <div className="pt-3">
                                    <div className="settings-form">
                                       <h4 className="text-primary">
                                          Ustawienia konta użytkownika
                                       </h4>
                              
                                       <form
                                          onSubmit={(e) => e.preventDefault()}
                                       >
                                          <div className="form-row">
                                             <div className="form-group col-md-6">
                                                <label>Nazwa użytkownika</label>
                                                <input
                                                disabled = {!userDetailsEditable}
                                                value = {userName}
                                                type="text"
                                                name="place"
                                                className="form-control"
                                                onChange =  {(e) => setUsername(e.target.value)}
                                                />
                                             </div>
                                             <div className="form-group col-md-6">
                                                <label>Email</label>
                                                <input
                                                disabled = {!userDetailsEditable}
                                                value = {email}
                                                   type="email"
                                                   className="form-control"
                                                   onChange =  {(e) => setEmail(e.target.value)}
                                                />
                                             </div>            
                                          </div>
                                          <div className="form-group">
                                             <label>Płeć</label>
                                             <Select
                                             isDisabled={!userDetailsEditable}
                                                 value={gender}
                                                 onChange={(e) => setGender(e)}
                                                 options={genderOptions}
                        style={{
                           lineHeight: "40px",
                           color: "#7e7e7e",
                           paddingLeft: " 15px",
                        }}  />
                                          </div>
                                          <div className="form-row">
                                             <div className="form-group col-md-6">
                                                <label>Data urodzenia</label>
                                              {loaded &&  <DatePicker disabled= {!userDetailsEditable} selected={dateOfBirth} onChange={date => setDateOfBirth(date)} />}
                                             </div>
                                             
                                          </div>

                                          <div className="form-group">
                                             <div className="custom-control custom-checkbox">
                                                <input
                                                   disabled = {!userDetailsEditable}
                                                   checked = {notificationsEnabled}
                                                   type="checkbox"
                                                   className="custom-control-input"
                                                   id="gridCheck"
                                                   onClick={() => handleNotificationEnabled()}
                                                />
                                                <label
                                                   className="custom-control-label"
                                                   htmlFor="gridCheck"
                                                >
                                                   Włącz powiadomienia o zaplanowanych treningach
                                                </label>
                                             </div>
                                          </div>

                                          <div className="row mb-2">
                                       <div className="col-sm-3">
                                          <h5 className="f-w-500">
                                             Data rejestracji
                                             <span className="pull-right d-none d-sm-block">
                                                :
                                             </span>
                                          </h5>
                                       </div>
                                       <div className="col-sm-9">
                                          <span>
                                            {loaded && new Date(userDetails.registrationDate).toLocaleDateString()}r.
                                          </span>
                                       </div>
                                    </div>

                                         {userDetailsEditable && <Button className="m-sm-right mt-2 d-flex align-items-right justify-content-center btn" variant="outline-primary" onClick = {() => {handleSaveUserDetails()}}>
                               Zapisz zmiany
                        </Button>}

                        {!userDetailsEditable && <Button className="m-sm-right mt-2 d-flex align-items-right justify-content-center btn" variant="outline-primary" onClick = {() => {setUserDetailsEditable(true)}}>
                               Edytuj szczegóły
                        </Button>}

                        <Button className="m-sm-right mt-2 d-flex align-items-right justify-content-center btn" variant="outline-primary" onClick = {() => {setChangePasswordModal(true)}}>
                               Zmień hasło
                        </Button>
                                       </form>
                                    </div>
                                 </div>
                              </div>
                           </div>
                        </div>
                     </div>
                  </div>
               </div>
            </div>
         </div>
      </Fragment>
   );
};

export default UserProfile;
