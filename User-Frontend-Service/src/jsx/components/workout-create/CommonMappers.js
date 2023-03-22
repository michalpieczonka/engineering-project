export function mapTrainingDay(day){
    switch(day){
        case 'MONDAY':
           return 'Poniedziałek';
        case 'TUESDAY':
           return 'Wtorek';
        case 'WEDNESDAY':
           return 'Środa';
        case 'THURSTDAY':
           return 'Czwartek';
        case 'FRIDAY':
           return 'Piątek';
        case 'SATURDAY':
           return 'Sobota';
        case 'SUNDAY':
           return 'Niedziela';
     }
}

export function mapPlanPriority(planPriority){
   switch(planPriority){
      case 'UNSPECIFIED':
         return 'Brak priorytetu';
      case 'LOWER':
         return 'Dół ciała';
      case 'LEGS':
            return 'Nogi';
      case 'CHEST':
            return 'Klatka piersiowa';
       case 'ARMS':
            return 'Ramiona';
      case 'BACK':
            return 'Plecy';      
      case 'SHOULDERS':
         return 'Barki';
      case 'HYBRID':
         return  "Hybrydowy";
   }
}

export function mapGender(gender){
   switch(gender){
      case 'MALE':
         return 'Mężczyzna';
      case 'FEMALE':
         return 'Kobieta';
      case 'unspecified':
         return 'Nieokreślony';
   }
}

export function genderToApi(gender){
   switch(gender){
      case 'Mężczyzna':
         return 'MALE';
      case 'Kobieta':
         return 'FEMALE';
   }
}

export function mapMuscleGroup(muscle){
   switch(muscle){
      case 'TRAPEZIUS':
         return 'Czworoboczny';
      case 'UPPER_BACK':
         return 'Najszerszy grzbietu';
         case 'LOWER_BACK':
            return 'Prostowniki';
      case 'BACK_DELTOIDS':
         return 'Naramienny tylny';
      case 'TRICEPS':
         return 'Triceps';
      case 'FOREARM':
         return 'Przedramie';
      case 'GLUTEAL':
         return 'Pośladek';
      case 'HAMSTRING':
         return 'Mięsień prosty uda';
         case 'ADDUCTOR':
            return 'Przywodziciel';
         case 'FOREARM':
            return 'Przedramie';
         case 'CALVES':
            return 'Łydki';
         case 'QUADRICEPS':
            return 'Czworogłowy uda';     
            case 'ABDUCTORS':
               return 'Odwodziciel';
            case 'FOREARM':
               return 'Przedramie';
            case 'ABS':
               return 'Brzuch';
            case 'OBLIQUES':
               return 'Mięśnie skośne brzucha';     
               case 'CHEST':
                  return 'Klatka piersiowa';
               case 'FRONT_DELTOIDS':
                  return 'Naramienny przedni';
               case 'BICEPS':
                  return 'Biceps';     
   }
}