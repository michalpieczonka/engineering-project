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