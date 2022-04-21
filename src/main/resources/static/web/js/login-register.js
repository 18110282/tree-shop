/*
 *
 * login-register modal
 * Autor: Creative Tim
 * Web-autor: creative.tim
 * Web script: http://creative-tim.com
 * 
 */
function showRegisterForm(){
    $('.loginBox').fadeOut('fast',function(){
        $('.registerBox').fadeIn('fast');
        $('.login-footer').fadeOut('fast',function(){
            $('.register-footer').fadeIn('fast');
        });
        $('.social').fadeIn('fast');
        $('.division').fadeIn('fast');
        $('.modal-title').html('&emsp;&ensp;Đăng ký tài khoản');

    });
    $('.sendVerifyBox').fadeOut('fast',function(){
        $('.registerBox').fadeIn('fast');
        $('.verify-footer').fadeOut('fast',function(){
            $('.register-footer').fadeIn('fast');
        });
        $('.modal-title').html('&emsp;&ensp;Đăng ký tài khoản');
    });
    $('.error').removeClass('alert alert-danger').html('');
       
}
function showSendVerifyForm(){
    $('.loginBox').fadeOut('fast',function(){
        $('.sendVerifyBox').fadeIn('fast');
        $('.login-footer').fadeOut('fast',function(){
            $('.verify-footer').fadeIn('fast');
        });
        $('.social').fadeOut('fast');
        $('.division').fadeOut('fast');
        $('.modal-title').html('&emsp;&ensp;Xác thực tài khoản');
    });
    $('.error').removeClass('alert alert-danger').html('');

}
function showLoginForm(){
    $('#loginModal .registerBox').fadeOut('fast',function(){
        $('.loginBox').fadeIn('fast');
        $('.register-footer').fadeOut('fast',function(){
            $('.login-footer').fadeIn('fast');    
        });
        $('.social').fadeIn('fast');
        $('.division').fadeIn('fast');
        
        $('.modal-title').html('&emsp;&emsp;Đăng nhập');
    });
    $('#loginModal .sendVerifyBox').fadeOut('fast',function(){
        $('.loginBox').fadeIn('fast');
        $('.verify-footer').fadeOut('fast',function(){
            $('.login-footer').fadeIn('fast');
        });

        $('.modal-title').html('&emsp;&emsp;Đăng nhập');
    });
    $('.error').removeClass('alert alert-danger').html('');
}

function openLoginModal(){
    showLoginForm();
    setTimeout(function(){
        $('#loginModal').modal('show');    
    }, 150);
    
}
function openRegisterModal(){
    showRegisterForm();
    setTimeout(function(){
        $('#loginModal').modal('show');    
    }, 150);
    
}
function openSendVerifyForm(){
    showSendVerifyForm();
    setTimeout(function(){
        $('#loginModal').modal('show');
    }, 150);

}


function loginAjax(){
    /*   Remove this comments when moving to server
    $.post( "/login", function( data ) {
            if(data == 1){
                window.location.replace("/home");            
            } else {
                 shakeModal(); 
            }
        });
    */

/*   Simulate error message from the server   */
     shakeModal();
}

function shakeModal(){
    $('#loginModal .modal-dialog').addClass('shake');
             $('.error').addClass('alert alert-danger').html("Invalid email/password combination");
             $('input[type="password"]').val('');
             setTimeout( function(){ 
                $('#loginModal .modal-dialog').removeClass('shake'); 
    }, 1000 ); 
}

   