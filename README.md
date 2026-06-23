# Sistema de Xadrez em Java

Projeto desenvolvido para a disciplina de Programação Orientada a Objetos. Implementa uma partida completa de xadrez via console, com todas as peças, regras oficiais e movimentos especiais.

---

## Estrutura do Projeto

O sistema é dividido em duas camadas independentes:

```
src/
├── application/
│   ├── Program.java       # Ponto de entrada e loop do jogo
│   └── UI.java            # Renderização do tabuleiro e leitura de entrada
├── boardgame/
│   ├── Board.java         # Tabuleiro genérico (matriz de peças)
│   ├── Piece.java         # Peça abstrata genérica
│   ├── Position.java      # Posição por linha e coluna (0-indexed)
│   └── BoardException.java
└── chess/
    ├── ChessMatch.java    # Controle da partida e regras do xadrez
    ├── ChessPiece.java    # Peça abstrata da camada de xadrez
    ├── ChessPosition.java # Posição no formato xadrez (ex: e4)
    ├── Color.java         # Enum das cores (WHITE, BLACK)
    ├── ChessException.java
    └── pieces/
        ├── King.java
        ├── Queen.java
        ├── Rook.java
        ├── Bishop.java
        ├── Knight.java
        └── Pawn.java
```

### Camada `boardgame`
Responsável pela estrutura genérica do tabuleiro. Não conhece nenhuma regra de xadrez — apenas gerencia a matriz de peças, valida posições e realiza operações de colocação e remoção.

### Camada `chess`
Responsável por todas as regras do jogo. Depende da camada `boardgame`, mas não o contrário — garantindo separação de responsabilidades.

---

## Regras Implementadas

### Movimentos das peças
| Peça | Movimentos |
|------|-----------|
| Rei (K) | Uma casa em qualquer direção |
| Rainha (Q) | Qualquer número de casas em qualquer direção |
| Torre (R) | Qualquer número de casas na horizontal e vertical |
| Bispo (B) | Qualquer número de casas nas diagonais |
| Cavalo (N) | Movimento em "L" (ignora peças intermediárias) |
| Peão (P) | Avanço de 1 casa (2 na abertura), captura diagonal |

### Movimentos especiais
- **Roque pequeno e grande:** o rei desloca 2 casas em direção à torre; condições: rei e torre sem movimento anterior, sem peças entre eles, rei não pode estar em xeque nem iniciar o roque em xeque.
- **En passant:** captura do peão adversário que avançou duas casas no turno imediatamente anterior.
- **Promoção do peão:** ao atingir a última fileira, o jogador escolhe entre Rainha (Q), Torre (R), Bispo (B) ou Cavalo (N).

### Controle de jogo
- Detecção de **xeque**: o jogador não pode realizar movimentos que exponham seu próprio rei.
- Detecção de **xeque-mate**: verifica se existe algum movimento legal que remova a ameaça ao rei; caso contrário, encerra a partida.
- Exibição de peças capturadas, turno atual e jogador em espera.

---

## Como Compilar e Executar

### Pré-requisitos
- Java JDK 11 ou superior

### Compilação
```bash
# Na raiz do projeto
javac -d bin -sourcepath src src/application/Program.java
```

### Execução
```bash
java -cp bin application.Program
```

### No Eclipse / IntelliJ
Importe o projeto, defina `src` como source folder e execute `Program.java`.

---

## Como Jogar

1. O tabuleiro é exibido com coordenadas `a–h` (colunas) e `1–8` (linhas).
2. Peças **brancas** aparecem em branco e peças **pretas** em amarelo.
3. Informe a **origem** da peça (ex: `e2`) e o **destino** (ex: `e4`).
4. As casas destacadas em azul indicam os movimentos possíveis da peça selecionada.
5. Para o **roque**, mova o rei duas casas na direção da torre (ex: `e1g1`).
6. Ao promover um peão, escolha a peça digitando `B`, `N`, `R` ou `Q`.

---

## Decisões de Projeto

- **Separação em camadas:** a camada `boardgame` é completamente independente do xadrez, permitindo reutilização para outros jogos de tabuleiro.
- **Referência ao ChessMatch em King e Pawn:** necessária para verificar o estado de xeque (roque) e o peão vulnerável ao en passant, sem violar o encapsulamento das demais peças.
- **Auto-promoção temporária para Rainha:** ao detectar promoção, `ChessMatch` substitui o peão por uma Rainha antes de calcular xeque e xeque-mate, garantindo que a peça promovida já entre nos cálculos. O jogador então escolhe a peça final.
- **Simulação em `testCheckMate`:** para cada peça da cor ameaçada, todos os movimentos possíveis são simulados via `makeMove`/`undoMove`; se nenhum remove o xeque, é xeque-mate.

---

## Autores

Desenvolvido em dupla como trabalho da disciplina de Programação Orientada a Objetos.
