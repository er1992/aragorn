$(document).ready(function(){
    var submit_form = $('.add-to-cart-form');

    submit_form.submit(function(event){
        event.preventDefault(); // stop the form doing what it wants to.

        // define all the constants used.
        var cart_icon = $('.cart_icon');
        var list_view = $("#list_btn").hasClass('d-block');
        var grid_add_button = $(this).find(".add-to-cart-btn-bg");
        grid_add_button.attr('disabled', true);

        var imgtodrag = list_view ? $(this).parent('div').siblings(".d-block").find("img") : $(this).parent("div").parent("div").siblings(".media-left").find("img");
        var addButton = list_view ? $(this).find("button").find("span").eq(1) : $(this).find(".btn-purchase").find("span");
        var itemPrice = list_view ? $(this).parent('div').find("h2") : $(this).siblings("h3");
        var mobile = window.innerWidth < 768;

        if (mobile){
                addButton.fadeOut(function(){
                    list_view ? $(this).html("Adding...").fadeIn() : $(this).removeClass("glyphicon-shopping-cart").addClass("glyphicon-option-horizontal").fadeIn();
                });
        }

        $.post($(this).attr('action'), $(this).serialize(), function(){
            // enable the add to cart buttons again
            grid_add_button.attr('disabled', false);
            if (mobile) { // if we're on a phone, let the user know by changing the button text
                addButton.fadeOut(function(){
                    list_view ? $(this).html("Item Added").fadeIn() : $(this).removeClass("glyphicon-option-horizontal").addClass("glyphicon-thumbs-up").fadeIn();
                    setTimeout(function(){
                        addButton.fadeOut(function(){
                            list_view ? $(this).html("Adding...").fadeIn() : $(this).removeClass("glyphicon-thumbs-up").addClass("glyphicon-shopping-cart").fadeIn();
                        })
                    }, 900);
                });
            } else { // if we're on desktop/tablet, show the image moving effect
                if (imgtodrag) {
                    var imgclone = imgtodrag.clone()
                        .offset({
                            top: imgtodrag.offset().top,
                            left: imgtodrag.offset().left
                        })
                        .css({
                            'opacity': '0.5',
                            'position': 'absolute',
                            'height': '150px',
                            'width': '150px',
                            'z-index': '100'
                        })
                        .appendTo($('body'))
                        .animate({
                            'top': cart_icon.offset().top + 10,
                            'left': cart_icon.offset().left + 10,
                            'width': 75,
                            'height': 75
                        }, 1000, 'easeInOutExpo');

                    // setTimeout(function () {
                    //     cart_icon.effect("shake", {
                    //         times: 2,
                    //         distance: 5
                    //     }, 200);
                    // }, 1500);

                    imgclone.animate({
                        'width': 0,
                        'height': 0
                    }, function () {
                        $(this).detach()
                    });
                }
            }
            // update the cart total regardless, even tho it's only seen on tablet/desktop.
            var itemCount = $("#cartItemCount");
            var itemWord = $("#cartItemWord");
            var totalPrice = $("#cartTotal");
            itemCount.html(parseInt(itemCount.html()) + 1);
            parseInt(itemCount.html()) == 1 ? itemWord.html('Item') : itemWord.html('Items');
            totalPrice.html('$' + (parseInt(totalPrice.html().match(/[\d]+/)) + parseInt(itemPrice.html().match(/[\d]+/))));

        });


    })

});
