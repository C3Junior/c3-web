 $(document).ready(function() {
    $(".template-upload").on("click",".start_upload",function(){
        var parent = $(this).parent().parent().parent();
        var progress_bar = parent.children(".progress_error").children(".progress");
        progress_bar.show();
    })
     $(".template-upload-replace").on("click",".start_upload",function(){
            var parent = $(this).parent().parent().parent();
            var progress_bar = parent.children(".progress_error").children(".progress");
            progress_bar.show();
        })
    $(".template-upload").on("keyup",".description", function(){
        var parent = $(this).parent().parent().parent();
        var start_button = parent.children(".tag_start_stop").children(".start_stop").children(".start").children();
        var description_text = $(this).val();
        if(description_text.length > 0){
            start_button.show();
        }
        else {start_button.hide();}
    })
    $('#file_upload_form').fileupload(
//     {
//        form: "file_upload",
//        autoUpload: false
//    }
    )

    $('#upload_form').modal({
        backdrop: true,
        keyboard: true,
        show: false
    })


    $('#file_replace_form').fileupload(
    {
             uploadTemplateId: 'template-upload-replace',
             downloadTemplateId: 'template-download-replace'
    });

    $('#replace_form').modal({
        backdrop: true,
        keyboard: true,
        show: false
    })

     $('.add-replace-file').change(function() {

           if ((this).files.length == 1)
                $(".fileinput-button").attr('disabled','disabled');
           else
                $(".fileinput-button").removeAttr('disabled')
       });
       
    $('.cancel-replace').click(function() {
        $(".fileinput-button").removeAttr('disabled')
     });
});