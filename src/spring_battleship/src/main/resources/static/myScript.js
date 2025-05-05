$(document).ready(function () {
    
    function createEmptyGrid(container) {
        for (let i = 0; i < 100; i++) {
            $(container).append('<div class="cell" data-index="' + i + '"></div>');
        }
    }

    createEmptyGrid('#player-grid');
    createEmptyGrid('#computer-grid');

    // Chiamata AJAX per ottenere le posizioni casuali
    $.ajax({
        url: '/api/popola-griglie',
        method: 'GET',
        success: function (response) {
            // response = { player: [1, 23, 45], computer: [10, 20, 30] }
            response.player.forEach(index => {
                $('#player-grid .cell').eq(index).addClass('ship');
            });
            response.computer.forEach(index => {
                $('#computer-grid .cell').eq(index).addClass('ship');
            });
        },
        error: function () {
            alert('Errore nel caricamento delle griglie!');
        }
    });

    // Aaggiungiamo il click sulle celle del campo del computer
    $('#computer-grid').on('click', '.cell', function () {
        const index = $(this).data('index');

        $.ajax({
            url: '/api/attacca/' + index,
            method: 'PUT',
            success: function (response) {
                if (response.hit) {
                    alert('Colpito!');
                    // coloriamo di rosso
                    $('#computer-grid .cell').eq(index).css('background-color', 'red');
                } else {
                    alert('Acqua!');
                    // coloriamo di grigio
                    $('#computer-grid .cell').eq(index).css('background-color', 'lightgrey');
                }
            },
            error: function () {
                alert('Errore nell\'attacco!');
            }
        });
    });
});