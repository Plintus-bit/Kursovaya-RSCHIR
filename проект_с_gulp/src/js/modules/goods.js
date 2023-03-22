window.addEventListener('click', function(event) {
    if (event.target.className == "img-full" && event.target.closest(".added_controls") != null) {
        let added_controls = event.target.closest(".added_controls");
        let action_type = event.target.closest("div");
        let input = added_controls.querySelector(".value_value");
        if (action_type.className == "controls_plus") {
            if (++input.value > input.max) {
                --input.value;
            }
        }
        else {
            if (--input.value < 0) {
                ++input.value;
            }
        }
    }
})