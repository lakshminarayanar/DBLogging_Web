$(function	()	{

	//Form Validation
	$('#basic-constraint').parsley( { listeners: {
        onFormSubmit: function ( isFormValid, event ) {
            if(isFormValid)	{
				return false;
			}
        }
    }}); 
	
	$('#type-constraint').parsley( { listeners: {
        onFormSubmit: function ( isFormValid, event ) {
            if(isFormValid)	{
				return false;
			}
        }
    }}); 
	 
	$('#formValidate1').parsley( { listeners: {
        onFormSubmit: function ( isFormValid, event ) {
            if(isFormValid)	{
				alert('Registration Complete');
				return false;
			}
        }
    }}); 
	
	$('#formValidate2').parsley( { listeners: {
        onFormSubmit: function ( isFormValid, event ) {
            if(isFormValid)	{
				alert('Your message has been sent');
				return false;
			}
        }
    }}); 
});