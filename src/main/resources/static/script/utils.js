document.addEventListener("DOMContentLoaded", () => {
    var data_category = [
        {
            "title": "Laptop",
            "image_link": "../images/icon/laptop.png",
            "sub_menu": [{"tilte": "Item 1"}]
        },
        {
            "title": "Smartphone",
            "image_link": "../images/icon/smartphone-call.png",
            "sub_menu": [{"tilte": "Item 1"}]
        },
        {
            "title": "Ipad",
            "image_link": "../images/icon/hand-touching-tablet-screen.png",
            "sub_menu": [{"tilte": "Item 1"}]
        },
        {
            "title": "Headphone",
            "image_link": "../images/icon/headphone-symbol.png",
            "sub_menu": [{"tilte": "Item 1"}]
        },
        {
            "title": "Bluetooth",
            "image_link": "../images/icon/earbuds.png",
            "sub_menu": [{"tilte": "Item 1"}]
        },
        {
            "title": "Microphone",
            "image_link": "../images/icon/microphone.png",
            "sub_menu": [{"tilte": "Item 1"}]
        },
        {
            "title": "Game console",
            "image_link": "../images/icon/console.png",
            "sub_menu": [{"tilte": "Item 1"}]
        },
        {
            "title": "Mouse",
            "image_link": "../images/icon/mouse.png",
            "sub_menu": [{"tilte": "Item 1"}]
        },
        {
            "title": "Charging device",
            "image_link": "../images/icon/smartphone.png",
            "sub_menu": [{"tilte": "Item 1"}]
        },
        {
            "title": "USB",
            "image_link": "../images/icon/usb-drive.png",
            "sub_menu": [{"tilte": "Item 1"}]
        },
    ]


    function init(){
        renderCategory(data_category)
        hideDropdownWhenClickSubMenu()
    }
    init()


    function renderCategory(data_category){
        let html = ""

        for(category of data_category){
            let html_sub_menu = ""
            if(category.sub_menu.length > 0){
                for(let sub_menu of category.sub_menu){
                    html_sub_menu += `<li><a href="#" class="dropdown-item dropdown-item-lv2">${sub_menu.tilte}</a></li>`
                }
            }
            html += `
            <li class="dropend list_category">
                <a href="#" class="dropdown-item dropdown-item-lv1" data-bs-toggle="dropdown">
                    <img src="${category.image_link}" alt="">
                    <span>${category.title}</span>
                    <svg xmlns="http://www.w3.org/2000/svg" height="15px" viewBox="0 -960 960 960" width="15px" fill="#000000"><path d="M579-480 285-774q-15-15-14.5-35.5T286-845q15-15 35.5-15t35.5 15l307 308q12 12 18 27t6 30q0 15-6 30t-18 27L356-115q-15 15-35 14.5T286-116q-15-15-15-35.5t15-35.5l293-293Z"/></svg>
                </a>
                ${html_sub_menu!=""?`<ul class="dropdown-menu dropdown-lv2">${html_sub_menu}</ul>`: ""}
            </li>
            `
        }

        document.querySelector(".dropdown-category")?.insertAdjacentHTML("beforeend", html)
    }


    function hideDropdownWhenClickSubMenu(){
        document.querySelector('.dropdown-category').addEventListener('click', function (event) {
            if (event.target && event.target.classList.contains('dropdown-item-lv2')) {
                document.querySelector("button.category_block")?.click()
            }
        });
    }
});

function renderBreadCrumbList(category_tree){
    let html = `
        <div><img src="../images/icon/image_home.png" class="img_home" alt=""></div>
        <div class="arrow-custom"></div>
    `
    for(let cate of category_tree){
        html += `
            <div class="ms-2">${cate}</div>
            <div class="arrow-custom"></div>
        `
    }
    document.querySelector(".bread_crumb_list").innerHTML = html
}


// slide show------------------
let slideIndex = 0;
var __banners = []
function renderSlideShow(banners) {
    __banners = banners
    const slidesWrapper = document.getElementById('slides-wrapper');
    const dotsContainer = document.getElementById('dots-container');
    
    banners.forEach((banner, index) => {
        // Tạo mỗi slide
        const slideDiv = document.createElement('div');
        slideDiv.classList.add('mySlide');
        slideDiv.innerHTML = `<img src="${banner}" alt="Banner ${index + 1}">`;
        slidesWrapper.appendChild(slideDiv);

        // Tạo dot tương ứng
        const dot = document.createElement('span');
        dot.classList.add('dot');
        dot.setAttribute('onclick', `currentSlide(${index})`);
        dotsContainer.appendChild(dot);
    });
}

function showSlides(index) {
    const slidesWrapper = document.getElementById('slides-wrapper');
    const dots = document.getElementsByClassName('dot');
    
    if (index >= __banners.length) {
        slideIndex = 0;
    } else if (index < 0) {
        slideIndex = __banners.length - 1;
    } else {
        slideIndex = index;
    }

    slidesWrapper.style.transform = `translateX(-${slideIndex * 100}%)`;

    for (let i = 0; i < dots.length; i++) {
        dots[i].className = dots[i].className.replace(" active", "");
    }
    dots[slideIndex].className += " active";
}

function plusSlides(n) {
    showSlides(slideIndex + n);
}

// Thumbnail image controls
function currentSlide(n) {
    showSlides(n);
}

// Automatic slideshow
setInterval(function() {
    plusSlides(1);
}, 5000); // Chuyển slide mỗi 5 giây

// --------------------------------