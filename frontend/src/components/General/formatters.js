export const formatWeekday = (weekday) => {
    const days = {
      MONDAY: 'Понедельник',
      TUESDAY: 'Вторник',
      WEDNESDAY: 'Среда',
      THURSDAY: 'Четверг',
      FRIDAY: 'Пятница',
      SATURDAY: 'Суббота',
      SUNDAY: 'Воскресенье'
    };
    return days[weekday] || weekday;
  };
  

  export const formatWeekType = (weekType) => {
    const types = {
      HIGH: 'Верхняя',
      LOW: 'Нижняя',
      BOTH: 'Обе',
      ODD: 'Нечётная',
      EVEN: 'Чётная'
    };
    return types[weekType] || weekType;
  };
  

  export const formatLessonType = (lessonType) => {
    const types = {
      LECTURE: 'Лекция',
      PRACTICE: 'Практика',
      LAB: 'Лабораторная',
      SEMINAR: 'Семинар',
      EXAM: 'Экзамен'
    };
    return types[lessonType] || lessonType;
  };
  
  export const formatTime = (timeString) => {
    if (!timeString) return '';
    return timeString.length >= 5 ? timeString.slice(0, 5) : timeString;
  };
  

  export const formatDate = (date) => {
    const options = { 
      weekday: 'long', 
      day: 'numeric', 
      month: 'long',
      year: 'numeric'
    };
    
    try {
      const dateObj = date instanceof Date ? date : new Date(date);
      return dateObj.toLocaleDateString('ru-RU', options);
    } catch (e) {
      console.error('Ошибка форматирования даты:', e);
      return date;
    }
  };
  
  
  export const formatGroups = (groups, propName = 'name') => {
    if (!Array.isArray(groups)) return '';
    return groups.map(g => g[propName]).filter(Boolean).join(', ');
  };
  
  export default {
    formatWeekday,
    formatWeekType,
    formatLessonType,
    formatTime,
    formatDate,
    formatGroups
  };