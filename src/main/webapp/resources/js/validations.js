  function handleShowDialogRequest(xhr, status, args) { 
      if( args.notValid || args.validationFailed )  
              return;
        if((args.currentBalance ==-3.0) && (args.leaveType=='Annual') && !(args.isOneYearOver))
       { 
         noLeaveBalanceDialog.show();
         return;
       }
       if(!(args.currentBalance >=1.0) && (args.leaveType=='Annual') && !(args.isOneYearOver))
       { 
         currentLeaveBalanceDialog.show();
         return;
       }
  		}
  	  function displayWhichHalfDay(value){
	  	 if(value==0.5){
			var myClasses = document.querySelectorAll('.timings'),
	        i = 0,
	        l = myClasses.length;
	
	    	for (i; i < l; i++) {
	       	 myClasses[i].style.display = 'block';
	   	 	}
	  	}
	  	 else{
	  		var myClasses = document.querySelectorAll('.timings'),
	        i = 0,
	        l = myClasses.length;
	
	    	for (i; i < l; i++) {
	       	 myClasses[i].style.display = 'none';
	   	 	}
	  	 }
  	  }
    