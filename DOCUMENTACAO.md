# Documentação das Classes — Sistema de Xadrez

---

## Camada `boardgame`

### `Position`
Representa uma posição no tabuleiro por linha e coluna (índices 0-based).

| Método | Descrição |
|--------|-----------|
| `Position(int row, int column)` | Construtor |
| `getRow()` / `getColumn()` | Retorna linha ou coluna |
| `setValues(int row, int column)` | Atualiza os valores da posição |
| `toString()` | Representação textual para depuração |

---

### `Piece`
Classe abstrata base para qualquer peça de tabuleiro.

| Membro | Descrição |
|--------|-----------|
| `position` | Posição atual da peça no tabuleiro (null se capturada) |
| `getBoard()` | Retorna o tabuleiro ao qual a peça pertence |
| `possibleMoves()` | Abstrato — retorna matriz booleana com casas alcançáveis |
| `possibleMove(Position)` | Verifica se uma posição específica é um movimento válido |
| `isThereAnyPossibleMove()` | Retorna true se existe ao menos um movimento possível |

---

### `Board`
Gerencia a matriz de peças e operações sobre o tabuleiro.

| Método | Descrição |
|--------|-----------|
| `Board(int rows, int columns)` | Construtor; lança `BoardException` se dimensões inválidas |
| `piece(int row, int column)` | Retorna a peça na posição indicada |
| `piece(Position)` | Sobrecarga com objeto Position |
| `placePiece(Piece, Position)` | Coloca uma peça; lança exceção se a casa estiver ocupada |
| `removePiece(Position)` | Remove e retorna a peça; retorna null se a casa estiver vazia |
| `positionExists(Position)` | Verifica se a posição está dentro do tabuleiro |
| `thereIsAPiece(Position)` | Verifica se há uma peça na posição |

---

### `BoardException`
Exceção não verificada lançada pela camada de tabuleiro para erros de posição ou colocação de peças.

---

## Camada `chess`

### `Color`
Enumerador com os valores `WHITE` e `BLACK`, representando as cores das peças.

---

### `ChessPosition`
Representa uma posição no formato xadrez (coluna `a–h`, linha `1–8`) e realiza a conversão para `Position` (0-based).

| Método | Descrição |
|--------|-----------|
| `ChessPosition(char column, int row)` | Construtor; valida o intervalo da posição |
| `toPosition()` | Converte para `Position` interna (0-based) |
| `fromPosition(Position)` | Converte de `Position` para `ChessPosition` (estático) |
| `toString()` | Retorna no formato `e4` |

---

### `ChessException`
Exceção não verificada lançada pela camada de xadrez para violações das regras do jogo (ex: mover peça do adversário, expor o próprio rei).

---

### `ChessPiece`
Classe abstrata que estende `Piece`, adicionando cor e contador de movimentos.

| Membro | Descrição |
|--------|-----------|
| `getColor()` | Retorna a cor da peça |
| `getMoveCount()` | Retorna quantas vezes a peça foi movida |
| `getChessPosition()` | Retorna a posição atual no formato xadrez |
| `isThereOpponentPiece(Position)` | Verifica se há uma peça adversária na posição |
| `increaseMoveCount()` | Incrementa o contador de movimentos |
| `decreaseMoveCount()` | Decrementa o contador (usado no desfazer) |

---

### `ChessMatch`
Classe central que controla o estado e as regras da partida.

| Membro | Descrição |
|--------|-----------|
| `getTurn()` | Número do turno atual |
| `getCurrentPlayer()` | Cor do jogador que deve mover |
| `getCheck()` | Indica se o jogador atual está em xeque |
| `getCheckMate()` | Indica se a partida foi encerrada por xeque-mate |
| `getEnPassantVulnerable()` | Retorna o peão vulnerável ao en passant (ou null) |
| `getPromoted()` | Retorna a peça promovida aguardando substituição (ou null) |
| `getPieces()` | Retorna a matriz 8×8 com as peças atuais |
| `possibleMoves(ChessPosition)` | Retorna a matriz de movimentos possíveis da peça na posição |
| `performChessMove(ChessPosition, ChessPosition)` | Executa um movimento, valida regras, atualiza estado e retorna peça capturada |
| `replacePromotedPiece(String)` | Substitui o peão promovido pela peça escolhida (`B`, `N`, `R` ou `Q`) |

**Métodos privados relevantes:**

| Método | Descrição |
|--------|-----------|
| `makeMove(Position, Position)` | Realiza o movimento (inclui roque e en passant) |
| `undoMove(Position, Position, Piece)` | Desfaz o movimento (usado na validação de xeque) |
| `testCheck(Color)` | Verifica se o rei da cor está em xeque |
| `testCheckMate(Color)` | Verifica se não existe nenhum movimento legal para sair do xeque |
| `initialSetup()` | Posiciona todas as peças na configuração inicial |

---

## Camada `chess.pieces`

### `King` — Rei
Move-se uma casa em qualquer direção. Possui referência a `ChessMatch` para verificar o estado de xeque durante o cálculo do roque.

- **Roque pequeno:** rei desloca 2 casas para o lado do rei (g1/g8).
- **Roque grande:** rei desloca 2 casas para o lado da rainha (c1/c8).
- Condições: rei e torre sem movimentos anteriores, casas intermediárias livres, rei não em xeque.

---

### `Queen` — Rainha
Combina os movimentos da Torre e do Bispo: qualquer número de casas em qualquer uma das 8 direções.

---

### `Rook` — Torre
Move-se qualquer número de casas na horizontal ou vertical. Usada também no roque (movida automaticamente por `ChessMatch`).

---

### `Bishop` — Bispo
Move-se qualquer número de casas nas diagonais. Cada bispo permanece sempre nas casas de uma única cor.

---

### `Knight` — Cavalo
Move-se em "L": 2 casas em uma direção e 1 casa perpendicular. Única peça capaz de saltar sobre outras.

---

### `Pawn` — Peão
Avança 1 casa (ou 2 casas no primeiro movimento) e captura na diagonal. Possui referência a `ChessMatch` para verificar en passant.

- **En passant (branco):** disponível quando o peão branco está na linha 5 (row=3) e o adversário avançou 2 casas no turno anterior.
- **En passant (preto):** disponível quando o peão preto está na linha 4 (row=4).
- **Promoção:** detectada em `ChessMatch` ao atingir a última fileira; o jogador escolhe a peça substituta.

---

## Camada `application`

### `UI`
Classe utilitária estática responsável pela interface via console.

| Método | Descrição |
|--------|-----------|
| `clearScreen()` | Limpa o terminal |
| `readChessPosition(Scanner)` | Lê e valida uma posição no formato `e4` |
| `printMatch(ChessMatch, List)` | Exibe tabuleiro, peças capturadas, turno e estado (xeque/xeque-mate) |
| `printBoard(ChessPiece[][])` | Exibe o tabuleiro sem destaques |
| `printBoard(ChessPiece[][], boolean[][])` | Exibe o tabuleiro com casas alcançáveis destacadas em azul |

---

### `Program`
Ponto de entrada da aplicação. Contém o loop principal do jogo:

1. Exibe o estado atual da partida.
2. Lê a posição de origem e exibe os movimentos possíveis.
3. Lê a posição de destino e executa o movimento.
4. Verifica se há promoção pendente e solicita escolha ao jogador.
5. Trata exceções de regra (`ChessException`) e de entrada (`InputMismatchException`).
6. Encerra exibindo o tabuleiro final quando há xeque-mate.
