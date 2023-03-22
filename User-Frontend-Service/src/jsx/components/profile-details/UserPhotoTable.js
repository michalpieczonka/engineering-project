import React, { useState,  useEffect } from "react";
import { Button } from "react-bootstrap";
import {deleteUserPhoto, getUserPhoto } from '../../../services/UserDetailsService'

const UserPhotoTable = ({photos, photosLoaded, refresh}) => {
    if(photosLoaded){
        return <h2>Trwa Wczytywanie zdjęć...</h2>
    } 

    const sortedData = photos.sort((a, b) => b.uploadedTime - a.uploadedTime);

    const handleRemovePhoto = (photo) => {

        swal({
            title: "Usuwanie pliku",
            text: "Czy napewno chcesz usunąć wybrany plik ?",
            icon: "warning",
            buttons: [
              'Nie, anuluj',
              'Tak, usuń'
            ],
            dangerMode: false,
          }).then(function(isConfirm) {
            if (isConfirm) {
               deleteUserPhoto(photo.photoId)
               .then(response => {
                  if(response.status == 204){
                     swal('Sukces', 'Plik został usunięty !', "success");
                     refresh()
                  } else {
                     swal('Błąd', 'Wystąpił błąd, plik nie mógł zostać usunięty !.', "error");
                  }
                  
               })
               .catch(error =>{
                  swal('Błąd', 'Wystąpił błąd w trakcie usuwania pliku ', "error");
                  console.log(error)})
   
            } 
          });
    }


    const downloadFile = (userPhotoId) => {

      console.log(userPhotoId + 'pobieram')
        getUserPhoto(userPhotoId)
          .then(response => {
            handleDownload(response.data.imageData, response.data.name);
          })
          .catch(error => {
            console.error(error);
          });
    
   }
      const handleDownload = (responseData, fileName) => {
        if (!responseData) {
          return;
        }
    

        const blob = new Blob([responseData], { type: 'application/octet-stream' });
        const url = URL.createObjectURL(blob);
        const link = document.createElement('a');

        link.href = url;
        link.download = fileName;
        link.click();
      };

  return (
    <tbody>
              {
                              sortedData.map((photo, index) => {
                                return  (
                                    <tr key = {photo.photoId}>
                                   <td>  {photo.uploadedTime} </td>
                                    <td> {photo.name} </td>

                           <td>
                           <Button variant="primary tp-btn"  onClick = {() => downloadFile(photo.photoId)}>Pobierz</Button>
                   </td>
                              <td>
                              <Button variant="danger tp-btn" onClick = {() => handleRemovePhoto(photo)}>Usuń</Button>
                        
                              </td>
           
                    
                        
                                 </tr>
)
                              })
                           }</tbody>
  )
}

export default UserPhotoTable
