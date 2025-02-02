/**
 * 
 */

console.log("mohammed azhar");

const togglesidebar = () => {
    if ($(".sidebar").is(":visible")) {
        $(".sidebar").css("display", "none");
        $(".content").css("margin-left", "0%");
    } else {
        $(".sidebar").css("display", "block");
        $(".content").css("margin-left", "20%");
    }
};


const search = () =>{
    // console.log("searching");

    let query =$("#search-input").val();
    

    if(query=="")
    {
        $(".search-result").hide();
    }
    else{
        console.log(query);

        //sending request to server
        let url =`http://localhost:8080/search/${query}`;

        fetch(url).then((response) =>{
                return response.json();
        }).then((data) =>{
            //data access
            console.log(data);

            let text=`<div class='list-group'>`
                    data.forEach((contact) => {
                        text+= `<a href='/user/contact/${contact.cId}' class='list-group-item list-group-action'> ${contact.name}</a>`;
                    });
            text+=`</div>`;

            $(".search-result").html(text);
            $(".search-result").show();

        });


       
    }
}