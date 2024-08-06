import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class MetropolisesView extends JFrame {
    private JTextField metropolis;
    private JTextField continent;
    private JTextField population;
    private JButton addButton;
    private JButton searchButton;
    private Box comboBoxBox;
    private JComboBox<String> populationPulldown;
    private JComboBox<String> matchTypePulldown;
    private MetropolisesModel metropolisModel;

    public MetropolisesView(String metropolisViewer) {
        super(metropolisViewer);
        setResizable(false);
        setLayout(new BorderLayout(4, 4));

        JPanel upper = new JPanel();
        JLabel metropolisesLabel = new JLabel("Metropolis");
        JLabel continentsLabel = new JLabel("Continent");
        JLabel populationsLabel = new JLabel("Population");
        metropolis = new JTextField(15);
        continent = new JTextField(15);
        population = new JTextField(15);
        upper.add(metropolisesLabel);
        upper.add(metropolis);
        upper.add(continentsLabel);
        upper.add(continent);
        upper.add(populationsLabel);
        upper.add(population);

        metropolisModel = new MetropolisesModel();
        JTable mainTable = new JTable();
        mainTable.getTableHeader().setOpaque(false);
        mainTable.getTableHeader().setBackground(Color.CYAN);
        mainTable.setModel(metropolisModel);
        mainTable.getTableHeader().setReorderingAllowed(false);
        mainTable.setVisible(true);
        JScrollPane jsp = new JScrollPane(mainTable);
        jsp.setWheelScrollingEnabled(true);
        jsp.setSize(new Dimension(300, 400));

        JLabel empty = new JLabel(" ");
        Box eastPanel = Box.createVerticalBox();
        addButton = new JButton("Add");
        searchButton = new JButton("Search");

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                metropolisModel.add(metropolis.getText(), continent.getText(), population.getText());
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean largerOrNot = Objects.equals(populationPulldown.getSelectedItem(), "Population Larger Than");
                boolean exact = Objects.equals(matchTypePulldown.getSelectedItem(), "Exact Match");
                metropolisModel.search(metropolis.getText(), continent.getText(), population.getText(),
                        largerOrNot, exact);
            }
        });

        eastPanel.add(addButton);
        eastPanel.add(searchButton);
        eastPanel.add(empty);
        comboBoxBox = Box.createVerticalBox();
        comboBoxBox.setBorder(new TitledBorder("Search Options"));
        String[] populationPulldownOptions = {"Population Larger Than", "Population Smaller Than or Equal To"};
        populationPulldown = new JComboBox<>(populationPulldownOptions);
        populationPulldown.setEditable(false);
        // populationPulldown.setSize(populationPulldown.getPreferredSize());
        String[] matchTypeOptions = {"Exact Match", "Partial Match"};
        matchTypePulldown = new JComboBox<>(matchTypeOptions);
        matchTypePulldown.setEditable(false);
        comboBoxBox.add(populationPulldown);
        comboBoxBox.add(matchTypePulldown);
        eastPanel.add(comboBoxBox);


        add(upper, BorderLayout.NORTH);
        add(eastPanel, BorderLayout.EAST);
        add(jsp, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }
}